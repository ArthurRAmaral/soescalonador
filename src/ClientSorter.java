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
				if(c2.getPriority() == c1.getPriority()) {
					return c1.getArrivalTime().compareTo(c2.getArrivalTime());
				} else if(c2.getArrivalTime().equals(c1.getArrivalTime())) {
						return c2.getPriority() - c1.getPriority();
					} else {
						return c1.getArrivalTime().compareTo(c2.getArrivalTime());
					}
				
			}
		});
	}

}
