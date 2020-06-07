import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.Semaphore;

public class Fifo implements Method {

    private List<Integer> responseTimes;
    private List<Integer> returnTimes;
    private String name = "FIFO";
    private static final int CLERKS = 2;
    private static LocalTime actual;

    public Fifo() {
        responseTimes = new ArrayList<>();
        returnTimes = new ArrayList<>();
    }

    @Override
    public int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd) {
        ClientSorter.sortByArrive(list);
        actual = dayStart;
        int clientsFinalized = 0;

        for (Client client : list) {

            LocalTime wait = LocalTime.of(0, 0);

            if (client.getArrivalTime().compareTo(actual) >= 0) {
                actual = client.getArrivalTime();
            } else {
                wait = actual.minusHours(client.getArrivalTime().getHour()).minusMinutes(client.getArrivalTime().getMinute());
            }

            LocalTime startedAt = actual;
            LocalTime finalizeAt = startedAt.plusHours(client.getEstimatedTime().getHour()).plusMinutes(client.getEstimatedTime().getMinute());

            if (finalizeAt.isBefore(dayEnd)) {
                //System.out.println("Started at: " + startedAt + "\t|\t" + "Prioridade: " + client.getPriority() + "\t|\tFinalized at: " + finalizeAt);
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
    public int startThread(File database, LocalTime dayStart, LocalTime dayEnd, int qntClients, int loop) {
        actual = dayStart;
        List<Client> list = new ArrayList<>(qntClients);
        Semaphore listLock = new Semaphore(1);
        Semaphore countItems = new Semaphore(0);
        Producer producer = new Producer(database, list, listLock, countItems, qntClients);
        ConsumerFifo clerk1 = new ConsumerFifo(actual, dayEnd, list, listLock, countItems, (qntClients+1)/CLERKS, "Hellen");
        ConsumerFifo clerk2 = new ConsumerFifo(actual, dayEnd, list, listLock, countItems, qntClients/CLERKS, "Isa");

        try {
            producer.start();
            clerk1.start();
            clerk2.start();
            producer.join();
            clerk1.join();
            clerk2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.responseTimes = clerk1.getResponseTimes();
        this.responseTimes.addAll(clerk2.getResponseTimes());

        this.returnTimes = clerk1.getReturnTimes();
        this.returnTimes.addAll(clerk2.getReturnTimes());

        return qntClients - list.size();
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

    @Override
    public String getName() {
        return this.name;
    }

}
