import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClientSorter {

	public static void sortByArrive(List<Client> list) {
		Collections.sort(list, new Comparator<Client>() {
			@Override
			public int compare(Client c1, Client c2) {
				return c1.getArrivalTime().compareTo(c2.getArrivalTime());
			}
		});
	}

	public static void sortByEstimated(List<Client> list) {
		Collections.sort(list, new Comparator<Client>() {
			@Override
			public int compare(Client c1, Client c2) {
				return c1.getEstimatedTime().compareTo(c2.getEstimatedTime());
			}
		});
	}
	
	public static void sortByPriorityFifo(List<Client> list) {
		Collections.sort(list, new Comparator<Client>() {
			@Override
			public int compare(Client c1, Client c2) {
				LocalTime estimated = c1.getEstimatedTime();
				if(c2.getPriority() == c1.getPriority()) {
					return c1.getArrivalTime().compareTo(c2.getArrivalTime());
				} else if (c1.getArrivalTime().plusHours(estimated.getHour()).plusMinutes(estimated.getMinute()).compareTo(c2.getArrivalTime()) > 0){
					return c1.getArrivalTime().compareTo(c2.getArrivalTime());
				} else {
					return c2.getPriority() - c1.getPriority();
				}
			}
		});
	}

}
