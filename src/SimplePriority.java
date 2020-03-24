import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class SimplePriority implements Method {
    private List<Integer> responseTimes;
    private List<Integer> returnTimes;

    @Override
    public int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd) {
        int clientsFinalized = 0;
        responseTimes = new ArrayList<>();
        returnTimes = new ArrayList<>();
        LocalTime actual = dayStart;

        List<Client> priorityList = list.stream().sorted(Comparator.comparing(Client::getPriority).reversed()).collect(Collectors.toList());

        for (Client client : priorityList) {
            LocalTime startedAt = actual;
            LocalTime finalizeAt = startedAt.plusHours(client.getEstimatedTime().getHour()).plusMinutes(client.getEstimatedTime().getMinute());

            if (finalizeAt.isBefore(dayEnd)) {
                //System.out.println("Started at: " + startedAt + "\t|\t" + "Password: " + client.getCode() + "\t|\tFinalized at: " + finalizeAt);
                actual = finalizeAt;
                clientsFinalized++;
//                System.out.println("startedAt.getMinute() = " + ((startedAt.getMinute() - dayStart.getMinute()) + (startedAt.getHour() - dayStart.getHour())*60  ));
                responseTimes.add(
                        (
                                (
                                        startedAt.getMinute() - dayStart.getMinute()
                                ) + (
                                        startedAt.getHour() - dayStart.getHour()
                                ) * 60
                        )
                );
                returnTimes.add(
                        (
                                (
                                        finalizeAt.getMinute() - dayStart.getMinute()
                                ) + (
                                        finalizeAt.getHour() - dayStart.getHour()
                                ) * 60
                        )
                );
            }
        }

        return clientsFinalized;
    }

    @Override
    public double getResponseTime() {
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
}
