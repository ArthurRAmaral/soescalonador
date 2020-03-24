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
}
