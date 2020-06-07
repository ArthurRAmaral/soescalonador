import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.Semaphore;


public class SimplePriorityFifo implements Method {
    private List<Integer> responseTimes;
    private List<Integer> returnTimes;
    private String name =  "Simple Priority FIFO";
    private static LocalTime actual;
    private static final int CLERKS = 2;

    public SimplePriorityFifo() {
        responseTimes = new ArrayList<>();
        returnTimes = new ArrayList<>();
    }

    @Override
    public int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd) {
        int clientsFinalized = 0;
        int initSize = list.size();
        actual = dayStart;

       // ClientSorter.sortByArrive(list);

//        System.out.println(list + "\n\n\n");

        for (int i = 0; i < initSize; i++) {

            Client client = getNextCLientOf(list, actual);
            //System.out.println("\nclient = " + client);

            LocalTime wait = LocalTime.of(0, 0);

            if (client.getArrivalTime().compareTo(actual) >= 0) {
                actual = client.getArrivalTime();
            } else {
                wait = actual.minusHours(client.getArrivalTime().getHour()).minusMinutes(client.getArrivalTime().getMinute());
            }

            LocalTime startedAt = actual;
            LocalTime finalizeAt = startedAt.plusHours(client.getEstimatedTime().getHour()).plusMinutes(client.getEstimatedTime().getMinute());

            if (finalizeAt.isBefore(dayEnd)) {
                //System.out.println("Started at: " + startedAt + "\t|Should start at: " + client.getArrivalTime() + "\t\t|Prioridade: " + client.getPriority() + "\t|\tFinalized at: " + finalizeAt + "\t|\tEstimate: " + client.getEstimatedTime());
                actual = finalizeAt;
                clientsFinalized++;
                //System.out.println("startedAt.getMinute() = " + ((startedAt.getMinute() - dayStart.getMinute()) + (startedAt.getHour() - dayStart.getHour())*60  ));
                responseTimes.add(
                        (
                                (
                                        wait.getMinute()
                                ) + (
                                        wait.getHour()
                                ) * 60
                        )
                );
                returnTimes.add(
                        (
                                (
                                        (finalizeAt.getMinute() - startedAt.getMinute()) + wait.getMinute()
                                ) + (
                                        (finalizeAt.getHour() - startedAt.getHour()) + wait.getHour()
                                ) * 60
                        )
                );
            }
        }

        return clientsFinalized;
    }

    private Client getNextCLientOf(List<Client> list, LocalTime actual) {
        Client returnClient = list.get(0);
        for (Client client : list) {
            if (client.getPriority() > returnClient.getPriority()) {
                if (client.getArrivalTime().compareTo(actual) <= 0||client.getArrivalTime().compareTo(returnClient.getArrivalTime()) <= 0) {
                    returnClient = client;
                }
            }
        }
        list.remove(returnClient);
        return returnClient;
    }

    @Override
    public int startThread(File database, LocalTime dayStart, LocalTime dayEnd, int qntClients, int loop) {
        actual = dayStart;
        List<Client> list = new ArrayList<>(qntClients);
        Semaphore listLock = new Semaphore(1);
        Semaphore countItems = new Semaphore(0);
        Producer producer = new Producer(database, list, listLock, countItems, qntClients);
        ConsumerPriorityFifo clerk1 = new ConsumerPriorityFifo(actual, dayEnd, list, listLock, countItems, (qntClients+1)/CLERKS, "Hellen");
        ConsumerPriorityFifo clerk2 = new ConsumerPriorityFifo(actual, dayEnd, list, listLock, countItems, (qntClients+1)/CLERKS, "Isa");

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

        this.responseTimes = clerk1.getResponseTimes();
        this.responseTimes.addAll(clerk2.getResponseTimes());

        this.returnTimes = clerk1.getReturnTimes();
        this.returnTimes.addAll(clerk2.getReturnTimes());

        return qntClients - list.size();
    }

    @Override
    public double getResponseTime() {
        //System.out.println(responseTimes);
        OptionalDouble average = responseTimes.stream().mapToInt(Integer::valueOf).average();
        if (average.isPresent()) return average.getAsDouble();
        return 0;
    }

    @Override
    public double getReturnTime() {
        OptionalDouble average = returnTimes.stream().mapToInt(Integer::valueOf).average();
        if (average.isPresent()) return average.getAsDouble();
        return 0;
    }
    
	@Override
	public String getName() {
    	return this.name;
    }
}
