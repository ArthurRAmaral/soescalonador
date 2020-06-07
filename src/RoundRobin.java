import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RoundRobin implements Method {

	private LocalTime actual;
	private LocalTime quantum;
	private List<LocalTime> returnTime;
	private String name =  "Round Robin";
	private static final int CLERKS = 2;

	public RoundRobin(LocalTime quantum) {
		this.quantum = quantum;
		this.returnTime = new ArrayList<>();
	}

	@Override
	public int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd) {
		List<Client> listClone = new ArrayList<>(list);

		ClientSorter.sortByArrive(listClone);
		int times = 0;
		int clientsFinalized = 0;
		actual = dayStart;
		LocalTime lastActual = actual;

		while (actual.compareTo(dayEnd) <= 0) {
			
			for (Client c : listClone) {
				if (c.getArrivalTime().isBefore(actual) && c.getEstimatedTime().compareTo(quantum) >= 0 && c.getArrivalTime().isAfter(dayStart)) {
					c.setEstimatedTime(c.getEstimatedTime().minusHours(quantum.getHour()).minusMinutes(quantum.getMinute()));
					times++;
					if(c.getEstimatedTime().compareTo(quantum) < 0) {
						System.out.println("\nFinalizing the client" + c.minimize() + " \nat: " + actual);
						clientsFinalized++;
						lastActual = actual;
						
						list.stream()
						.filter(client -> client.getCode().equals(c.getCode()))
						.forEach(client -> returnTime.add(actual.minusHours(client.getArrivalTime().getHour())
								.minusMinutes(client.getArrivalTime().getMinute())));
						
					}
					actual = actual.plusHours(quantum.getHour()).plusMinutes(quantum.getMinute());
				}
			}
			
			if(times == 0) {
				actual = actual.plusHours(quantum.getHour()).plusMinutes(quantum.getMinute());
			}
			
			times = 0;

		}
		actual = lastActual;
		return clientsFinalized;
	}

	public LocalTime getQuantum() {
		return quantum;
	}

	public void setQuantum(LocalTime quantum) {
		this.quantum = quantum;
	}

	public LocalTime getActual() {
		return this.actual;
	}

	@Override
	public int startThread(File database, LocalTime dayStart, LocalTime dayEnd, int qntClients, int loopTimes) {
		actual = dayStart;
		List<Client> list = new ArrayList<>(qntClients);
		Semaphore listLock = new Semaphore(1);
		Semaphore countItems = new Semaphore(0);
		Producer producer = new Producer(database, list, listLock, countItems, qntClients);
		ConsumerRoundRobin clerk1 = new ConsumerRoundRobin(this.quantum, actual, dayEnd, list, listLock, countItems, (loopTimes+1)/CLERKS, "Hellen");
		ConsumerRoundRobin clerk2 = new ConsumerRoundRobin(this.quantum, actual, dayEnd, list, listLock, countItems, (loopTimes+1)/CLERKS, "Isa");

		try {
			producer.start();
			clerk1.start();
			clerk2.start();
			producer.join();
			clerk1.join();
			clerk2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.returnTime = clerk1.getReturnTimes();
		this.returnTime.addAll(clerk2.getReturnTimes());

		return qntClients - list.size();
	}


	@Override
	public double getResponseTime() {
		return 0;
	}

	@Override
	public double getReturnTime() {
		
		LocalTime total = LocalTime.of(0, 0);
		
		for(LocalTime t : returnTime) {
			total = total.plusHours(t.getHour()).plusMinutes(t.getMinute());
		}

		return ((total.getHour() * 60) + total.getMinute()) / (double) returnTime.size();
	}
	
	@Override
	public String getName() {
    	return this.name;
    }

}
