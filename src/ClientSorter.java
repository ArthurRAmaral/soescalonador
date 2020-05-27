import java.time.LocalTime;
import java.util.ArrayList;
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

//	public static void sortByEstimated(List<Client> list) {
//		Collections.sort(list, new Comparator<Client>() {
//			@Override
//			public int compare(Client c1, Client c2) {
//				return c1.getEstimatedTime().compareTo(c2.getEstimatedTime());
//			}
//		});
//	}
	
	public static void sortByEstimated(List<Client> list, LocalTime aux) {
		Collections.sort(list, new Comparator<Client>() {

			LocalTime actual = aux;
			List<String> ended = new ArrayList<String>();
			int index = 1;

			public void increase(Client c1, Client c2, int value) {
				if(c1.getCode().equals("I002")) {
					System.out.println("INDEX I002 ===== " + list.indexOf(c1));
				} else if (c2.getCode().equals("I002")) {
					System.out.println("INDEX I002 ===== " + list.indexOf(c2));
				}
				if(list.indexOf(c1) <= index || list.indexOf(c1) <= index) {
					index++;
				if (value < 0 && !ended.contains(c1.getCode())) {
					actual = actual.plusHours(c1.getEstimatedTime().getHour());
					actual = actual.plusMinutes(c1.getEstimatedTime().getMinute());
					ended.add(c1.getCode());
				} else if (!ended.contains(c2.getCode())) {
					actual = actual.plusHours(c2.getEstimatedTime().getHour());
					actual = actual.plusMinutes(c2.getEstimatedTime().getMinute());
					ended.add(c2.getCode());
				}
				}
				System.out.println("\nACTUAL = " + actual);
				
			}

			@Override
			public int compare(Client c1, Client c2) {
				 System.out.println("\n\n============\n\nValores: c1 = " + c1 + " \n\nc2 = " +
				 c2 );
				LocalTime first = c1.getArrivalTime().compareTo(c2.getArrivalTime()) > 0 ? c2.getArrivalTime()
						: c1.getArrivalTime();
				if (actual.compareTo(first) < 0) {
					actual = first;
				}

				if (actual.compareTo(c1.getArrivalTime()) > 0) {
					if (actual.compareTo(c2.getArrivalTime()) > 0) {
						System.out.println("caso 3");
						int chosen = c1.getEstimatedTime().compareTo(c2.getEstimatedTime());
						increase(c1, c2, chosen);
						return chosen;
					} else {
						System.out.println("caso 4");
						int chosen = c1.getArrivalTime().compareTo(c2.getArrivalTime());
						increase(c1, c2, chosen);
						return chosen;
					}
				} else if (c2.getArrivalTime().equals(c1.getArrivalTime())) {
					System.out.println("caso 2");
					int chosen = c1.getEstimatedTime().compareTo(c2.getEstimatedTime());
					increase(c1, c2, chosen);
					return chosen;
				} else {
					System.out.println("caso 5");
					int chosen = c1.getArrivalTime().compareTo(c2.getArrivalTime());
					increase(c1, c2, chosen);
					return chosen;
				}
	
			}

		});
	}

	public static void sortByPriorityFifo(List<Client> list, LocalTime aux) {

		list.sort(Comparator.comparing(Client::getPriority).reversed());
		list.sort(Comparator.comparing(Client::getArrivalTime));
		System.out.println("list = " + list);

//		Collections.sort(list, new Comparator<Client>() {
//
//			LocalTime actual = aux;
//			List<String> ended = new ArrayList<String>();
//			int index = 1;
//
//			public void increase(Client c1, Client c2, int value) {
//				if(list.indexOf(c1) == index || list.indexOf(c1) == index) {
//					index++;
//				if (value < 0 && !ended.contains(c1.getCode())) {
//					actual = actual.plusHours(c1.getEstimatedTime().getHour());
//					actual = actual.plusMinutes(c1.getEstimatedTime().getMinute());
//					ended.add(c1.getCode());
//				} else if (!ended.contains(c2.getCode())) {
//					actual = actual.plusHours(c2.getEstimatedTime().getHour());
//					actual = actual.plusMinutes(c2.getEstimatedTime().getMinute());
//					ended.add(c2.getCode());
//				}
//				}
//				System.out.println("\nACTUAL = " + actual);
//
//			}
//
//			@Override
//			public int compare(Client c1, Client c2) {
//				 System.out.println("\n\n============\n\nValores: c1 = " + c1 + " \n\nc2 = " +
//				 c2 );
//				LocalTime first = c1.getArrivalTime().compareTo(c2.getArrivalTime()) > 0 ? c2.getArrivalTime()
//						: c1.getArrivalTime();
//				if (actual.compareTo(first) < 0) {
//					actual = first;
//				}
//
//				if (c2.getPriority() == c1.getPriority()) {
//					System.out.println("caso 1");
//					int chosen = c1.getArrivalTime().compareTo(c2.getArrivalTime());
//					increase(c1, c2, chosen);
//					return chosen;
//				} else if (c2.getArrivalTime().equals(c1.getArrivalTime())) {
//					System.out.println("caso 2");
//					int chosen = c2.getPriority() - c1.getPriority();
//					increase(c2, c1, -1 * chosen);
//					return chosen;
//				} else if (actual.compareTo(c1.getArrivalTime()) > 0) {
//					if (actual.compareTo(c2.getArrivalTime()) > 0) {
//						System.out.println("caso 3");
//						int chosen = c2.getPriority() - c1.getPriority();
//						increase(c2,  c1, -1 * chosen);
//						return chosen;
//					} else {
//						System.out.println("caso 4");
//						int chosen = c1.getArrivalTime().compareTo(c2.getArrivalTime());
//						increase(c1, c2, chosen);
//						return chosen;
//					}
//
//				} else {
//					System.out.println("caso 5");
//					int chosen = c1.getArrivalTime().compareTo(c2.getArrivalTime());
//					increase(c1, c2, chosen);
//					return chosen;
//				}
//
//			}
//
//		});
	}

}
