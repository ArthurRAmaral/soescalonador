import java.io.File;
import java.time.LocalTime;
import java.util.List;

public interface Method {
    int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd);

    int startThread(File database, LocalTime dayStart, LocalTime dayEnd, int qntClients);

    double getResponseTime();

    double getReturnTime();

    default String getName() {
    	return "Unknown";
    }
}
