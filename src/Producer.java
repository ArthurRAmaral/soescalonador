import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.io.File;
import java.util.concurrent.Semaphore;

public class Producer extends Thread{
    private List<Client> producerList;
    private Semaphore lock;
    private Semaphore full;
    private int numOp;

    public Producer(List<Client> producerList, Semaphore sem, Semaphore count, int op){
        this.producerList = producerList;
        lock = sem;
        full = count;
        numOp = op;
    }

    public void run(File database) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(database));
        String line = br.readLine();
        for(int i=0; i<numOp; i++){
            String values[] = line.split(";");

            String code = values[0];
            String cpf = values[1].substring(0, 9) + "-" + values[1].substring(9);
            int priority = Integer.parseInt(values[2]);

            String[] time = values[3].split(":");
            LocalTime estimatedTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));

            time = values[4].split(":");
            LocalTime arrivalTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));

            Client tmpClient = new Client(code, cpf, priority, estimatedTime, arrivalTime);

            line = br.readLine();
            try{
                lock.acquire();
                producerList.add(tmpClient);
                full.release();
                lock.release();
            }catch(InterruptedException ie){}
            System.out.println(tmpClient);
        }
        br.close();
    }

}
