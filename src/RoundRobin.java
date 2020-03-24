import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoundRobin {

	private static LocalTime actual;
	
	public static int start(List<Client> list, LocalTime quantum, LocalTime dayStart, LocalTime dayEnd) {
		List<Client> listClone = new ArrayList<Client>(list);

		ClientSorter.sortByArrive(listClone);
		int times = 1;
		int clientsFinalized = 0;
		actual = dayStart;
		LocalTime lastActual = actual;

		while (actual.compareTo(dayEnd) <= 0) {
			
			for (Client c : listClone) {
				if (c.getArrivalTime().compareTo(actual) <= 0 && c.getEstimatedTime().compareTo(quantum) >= 0) {
					c.setEstimatedTime(c.getEstimatedTime().minusHours(quantum.getHour()).minusMinutes(quantum.getMinute()));
					times++;
					if(c.getEstimatedTime().compareTo(quantum) < 0) {
						//System.out.println("\nFinalizing the client" + c.minimize() + " \nat: " + actual);
						clientsFinalized++;
						lastActual = actual;
					}
				}
			}

			for (int i = 0; i < times; i++) {
				actual = actual.plusHours(quantum.getHour()).plusMinutes(quantum.getMinute());
			}
			times = 1;

		}
		actual = lastActual;
		return clientsFinalized;
	}

	public static LocalTime getActual() {
		return actual;
	}

}
