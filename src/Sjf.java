import java.time.LocalTime;
import java.util.List;

public class Sjf implements Method{

	@Override
	public int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd) {
		System.out.println(list);
		ClientSorter.sortByEstimated(list);
		System.out.println(list);
		return 0;
	}

	@Override
	public double getResponseTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getReturnTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
