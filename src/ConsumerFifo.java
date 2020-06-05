import java.util.List;
import java.util.concurrent.Semaphore;

public class ConsumerFifo extends Thread {

    private List<Client> consumerList;
    private Semaphore lock;
    private Semaphore full;
    private int numOp;
    private String name;

    public ConsumerFifo(List<Client> list, Semaphore sem, Semaphore count, int op, String name) {
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
                Client next = consumerList.get(0);
                consumerList.remove(0);
                System.out.println("------" + this.name + "-------" + next);
                lock.release();
            } catch (InterruptedException ie2) {
                ie2.printStackTrace();
            }
        }
    }
}
