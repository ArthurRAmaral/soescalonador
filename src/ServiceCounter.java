import java.util.HashMap;
import java.util.Map;

public class ServiceCounter {
    static Map<Service, Integer> counters = new HashMap<>(initMap());

    private static Map<Service, Integer> initMap() {
        Map<Service, Integer> map = new HashMap<>();
        Service.stream().forEach(service -> map.put(service, 0));
        return map;
    }

    static void addOneAt(Service service) {
        Integer cont = counters.get(service);
        cont += 1;
        counters.put(service, cont);
    }

    static Integer getCoutOF(Service service) {
        return counters.get(service);
    }
}
