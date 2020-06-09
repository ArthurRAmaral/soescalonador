import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ConsumerRoundRobin extends Thread {

    private List<Client> consumerList;
    private List<Integer> responseTimes;
    private List<LocalTime> returnTimes;
    private Semaphore lock;
    private Semaphore full;
    private LocalTime actual;
    private LocalTime dayEnd;
    private LocalTime end;
    private LocalTime quantum;
    private int numOp;
    private String name;

    public ConsumerRoundRobin(LocalTime quantum, LocalTime actual, LocalTime dayEnd, List<Client> list, Semaphore sem, Semaphore count, int op, String name) {
        this.consumerList = list;
        this.lock = sem;
        this.full = count;
        this.numOp = op;
        this.name = name;
        this.actual = actual;
        this.dayEnd = dayEnd;
        this.quantum = quantum;
        responseTimes = new ArrayList<>();
        returnTimes = new ArrayList<>();
        end = LocalTime.of(0, 0);
    }

    @Override
    public void run() {
        for (int i = 0; i < numOp - 1; i++) {
            try {
                full.acquire();
                lock.acquire();
                Client next = getNextClient();
                if (next == null) {
                    i--;
                }
                lock.release();
                full.release();
                sleep(this.quantum.getMinute() * 10);
            } catch (InterruptedException ie2) {
                ie2.printStackTrace();
            }
        }
    }

    public List<LocalTime> getReturnTimes() {
        return returnTimes;
    }

    public List<Integer> getResponseTimes() {
        return null;
    }

    private Client getNextClient() {
        if (consumerList.size() == 0) {
            return null;
        }

        Client client = consumerList.get(0);
        for (Client c : consumerList) {
            if (client.getArrivalTime().compareTo(actual) >= 0 && c.getArrivalTime().compareTo(actual) < 0) {
                client = c;
            }
        }

        consumerList.remove(client);

        if (actual.isBefore(client.getArrivalTime())) {
            actual = client.getArrivalTime();
        }

//        System.out.println("\n\nName = " + this.name+" --- Client: " + client);

        client.setEstimatedTime(client.getEstimatedTime().minusHours(quantum.getHour()).minusMinutes(quantum.getMinute()));
        actual = actual.plusHours(quantum.getHour()).plusMinutes(quantum.getMinute());

//             System.out.println("Now: " + actual);

        if (client.getEstimatedTime().compareTo(end) > 0) {
            consumerList.add(consumerList.size(), client);
        } else {
            this.returnTimes.add(actual.minusHours(client.getArrivalTime().getHour())
                    .minusMinutes(client.getArrivalTime().getMinute()));
           System.out.println("\n" + this.name + " --- Cliente = " + client.getCode() +
                    "\t\tShould start = " + client.getArrivalTime() + "\t\tFinalized at = " + actual +
                    "\t\tEstimated time = " + client.getEstimatedTime());
        }

        return client;
    }
}
