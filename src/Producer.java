import java.io.*;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Producer extends Thread{
    private List<Client> producerList;
    private File database;
    private Semaphore lock;
    private Semaphore full;
    private int numOp;
    private int lines;

    public Producer(File database, List<Client> producerList, Semaphore sem, Semaphore count, int op){
        this.producerList = producerList;
        this.database = database;
        this.lock = sem;
        this.full = count;
        this.numOp = op;
        this.lines = 0;
    }

    public void run() {

        for(int i=0; i<numOp; i++){
            try {
                lock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client tmpClient = null;
            try {
                tmpClient = readClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            producerList.add(tmpClient);
            lines++;

            full.release();
            lock.release();
        }
    }

    public Client readClient() throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(database));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;

        line = br.readLine();

        for(int l = 0; l < lines; l++){
            line = br.readLine();
        }

        String[] values = line.split(";");

        String code = values[0];
        String cpf = values[1].substring(0, 9) + "-" + values[1].substring(9);
        int priority = Integer.parseInt(values[2]);

        String[] time = values[3].split(":");
        LocalTime estimatedTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));

        time = values[4].split(":");
        LocalTime arrivalTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));

        Client tmpClient = new Client(code, cpf, priority, estimatedTime, arrivalTime);

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tmpClient;
    }
}
