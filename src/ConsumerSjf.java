import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class ConsumerSjf extends Thread {

    private List<Client> consumerList;
    private List<Integer> responseTimes;
    private List<Integer> returnTimes;
    private Semaphore lock;
    private Semaphore full;
    private LocalTime actual;
    private LocalTime dayEnd;
    private int numOp;
    private String name;

    public ConsumerSjf(LocalTime actual, LocalTime dayEnd, List<Client> list, Semaphore sem, Semaphore count, int op, String name) {
        this.consumerList = list;
        this.lock = sem;
        this.full = count;
        this.numOp = op;
        this.name = name;
        this.actual = actual;
        this.dayEnd = dayEnd;
        responseTimes = new ArrayList<>();
        returnTimes = new ArrayList<>();
    }


    @Override
    public void run() {
        for (int i = 0; i < numOp; i++) {
            try {
                full.acquire();
                lock.acquire();
                Client next = getNextClient();
                consumerList.remove(next);
                lock.release();
                sleep(next.getEstimatedTime().getMinute() * 10);
            } catch (InterruptedException ie2) {
                ie2.printStackTrace();
            }
        }
    }

    public List<Integer> getReturnTimes() {
        return returnTimes;
    }

    public List<Integer> getResponseTimes() {
        return responseTimes;
    }

    private Client getNextClient() {
        Client returnClient = consumerList.get(0);
        final Client compareClient = returnClient;
        List<Client> aux;

        if (returnClient.getArrivalTime().compareTo(actual) < 0) {
            aux = consumerList.stream().filter(c -> c.getArrivalTime().compareTo(actual) <= 0)
                    .collect(Collectors.toList());
        } else {
            aux = consumerList.stream().filter(c -> c.getArrivalTime().compareTo(compareClient.getArrivalTime()) <= 0).collect(Collectors.toList());
        }

        for (Client client : aux) {
            if (client.getEstimatedTime().compareTo(returnClient.getEstimatedTime()) < 0) {
                returnClient = client;
            }
        }

        LocalTime wait = LocalTime.of(0, 0);

        if (returnClient.getArrivalTime().compareTo(actual) >= 0) {
            actual = returnClient.getArrivalTime();
        } else {
            wait = actual.minusHours(returnClient.getArrivalTime().getHour()).minusMinutes(returnClient.getArrivalTime().getMinute());
        }

        LocalTime startedAt = actual;
        LocalTime finalizeAt = startedAt.plusHours(returnClient.getEstimatedTime().getHour()).plusMinutes(returnClient.getEstimatedTime().getMinute());

        if (finalizeAt.isBefore(dayEnd)) {
            System.out.println(this.name + " --- Started at: " + startedAt + "\t|Should start at: " + returnClient.getArrivalTime() + "\t\t|Prioridade: " + returnClient.getPriority() + "\t|\tFinalized at: " + finalizeAt + "\t|\tEstimate: " + returnClient.getEstimatedTime());
            actual = finalizeAt;
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

        return returnClient;
    }
}
