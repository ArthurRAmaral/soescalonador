import java.time.LocalTime;
import java.util.List;

public interface Method {
    int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd);

    double getResponseTime();

    double getReturnTime();


}
