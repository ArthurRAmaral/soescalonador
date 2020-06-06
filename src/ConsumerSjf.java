import java.util.List;
import java.util.concurrent.Semaphore;

public class ConsumerSjf extends Thread {

    private List<Client> consumerList;
    private Semaphore lock;
    private Semaphore full;
    private int numOp;
    private String name;

    public ConsumerSjf(List<Client> list, Semaphore sem, Semaphore count, int op, String name) {
        this.consumerList = list;
        this.lock = sem;
        this.full = count;
        this.numOp = op;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < numOp; i++) {
            try {
                full.acquire();
                lock.acquire();
                Client next = getNextClient();
                System.out.println("------" + this.name + "-------" + next);
                lock.release();
            } catch (InterruptedException ie2) {
                ie2.printStackTrace();
            }
        }
    }

    private Client getNextClient() {
        Client choosedClient = consumerList.get(0);
        for (Client client :
                consumerList) {
            if (client.getEstimatedTime().compareTo(choosedClient.getEstimatedTime()) < 0)
                choosedClient = client;
        }
        consumerList.remove(choosedClient);
        return choosedClient;
    }
}
