import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.HOURS;;

public class SimplePriority implements Method {
    private List<Integer> responseTimes;
    private List<Integer> returnTimes;

    public SimplePriority() {
        responseTimes = new ArrayList<>();
        returnTimes = new ArrayList<>();
    }
    
    @Override
    public int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd) {
        int clientsFinalized = 0;
        LocalTime actual = dayStart;
        
        ClientSorter.sortByArrive(list);

        ClientSorter.sortByPriorityFifo(list, dayStart);
        System.out.println(list + "\n\n\n");
        
        
        for (Client client : list) {
        	
        	LocalTime wait = LocalTime.of(0,0);
        	
        	if(client.getArrivalTime().compareTo(actual) >= 0) {
        		actual = client.getArrivalTime();
        	} else {
        		wait = actual.minusHours(client.getArrivalTime().getHour()).minusMinutes(client.getArrivalTime().getMinute());
        	}
        	        	
            LocalTime startedAt = actual;
            LocalTime finalizeAt = startedAt.plusHours(client.getEstimatedTime().getHour()).plusMinutes(client.getEstimatedTime().getMinute());

            if (finalizeAt.isBefore(dayEnd)) {
              System.out.println("Started at: " + startedAt + "\t|Should start at: " + client.getArrivalTime() + "\t\t|Prioridade: " + client.getPriority() + "\t|\tFinalized at: " + finalizeAt + "\t|\tEstimate: " + client.getEstimatedTime());
                actual = finalizeAt;
                clientsFinalized++;
               //System.out.println("startedAt.getMinute() = " + ((startedAt.getMinute() - dayStart.getMinute()) + (startedAt.getHour() - dayStart.getHour())*60  ));
                responseTimes.add(
                        (
                                (
                                        wait.getMinute()
                                ) + (
                                		wait.getHour()
                                ) * 60
                        )
                );
                returnTimes.add(
                        (
                        		(
                                        (finalizeAt.getMinute() - startedAt.getMinute()) + wait.getMinute()
                                ) + (
                                		(finalizeAt.getHour() - startedAt.getHour()) + wait.getHour()
                                ) * 60
                        )
                );
            }
                    }

        return clientsFinalized;
    }

    @Override
    public double getResponseTime() {
    	//System.out.println(responseTimes);
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
