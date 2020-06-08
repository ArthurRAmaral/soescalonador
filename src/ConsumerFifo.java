import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.Semaphore;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class ConsumerFifo extends Thread {

    private List<Client> consumerList;
    private List<Integer> responseTimes;
    private List<Integer> returnTimes;
    private Semaphore lock;
    private Semaphore full;
    private LocalTime actual;
    private LocalTime dayEnd;
    private int numOp;
    private String name;

    public ConsumerFifo(LocalTime actual, LocalTime dayEnd, List<Client> list, Semaphore sem, Semaphore count, int op, String name) {
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
                consumerList.remove(0);
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
        Client client = consumerList.get(0);

        LocalTime wait = LocalTime.of(0, 0);

        if (client.getArrivalTime().compareTo(actual) >= 0) {
            actual = client.getArrivalTime();
        } else {
            wait = LocalTime.of((int) HOURS.between(client.getArrivalTime(), actual), (int) MINUTES.between(client.getArrivalTime(), actual));
        }

        LocalTime startedAt = actual;
        LocalTime finalizeAt = startedAt.plusHours(client.getEstimatedTime().getHour()).plusMinutes(client.getEstimatedTime().getMinute());

        if (finalizeAt.isBefore(dayEnd)) {
            System.out.println(this.name + "-------Cliente = " + client.getCode() + "\t\tStarted at: " + startedAt + "\t|\t" + "Prioridade: " + client.getPriority() + "\t\tArrived: " + client.getArrivalTime() + "\t|\tFinalized at: " + finalizeAt);
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

        return client;
    }
}
