import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final String DATABASE_FILE = "testNoRepeat.txt";
    private static final LocalTime QUANTUM = LocalTime.of(0, 1);
    private static final LocalTime START = LocalTime.of(8, 0);
    private static final LocalTime END = LocalTime.of(17, 0);
    private static DecimalFormat df = new DecimalFormat("0.00");


    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        File databaseFile = new File(DATABASE_FILE);
        List<Client> database = FileConverter.getClientData(databaseFile);
        int time = database.stream().mapToInt(e -> e.getEstimatedTime().getHour() * 60 + e.getEstimatedTime().getMinute()).sum();

        int choose;

        do {
            menu();
            choose = input.nextInt();

            switch (choose) {
                case 1:
                    startMethod(databaseFile, new Fifo());
                    break;
                case 2:
                    startMethod(databaseFile, new Sjf());
                    break;
                case 3:
                    startMethod(databaseFile, new SimplePriorityFifo());
                    break;
                case 4:
                    startMethod(databaseFile, new SimplePrioritySjf());
                    break;
                case 5:
                    startMethod(databaseFile, new RoundRobin(QUANTUM));
                    break;
                case 6:
                    startThread(databaseFile, new Fifo(), database.size(), database.size());
                    break;
                case 7:
                    startThread(databaseFile, new Sjf(), database.size(), database.size());
                    break;
                case 8:
                    startThread(databaseFile, new SimplePriorityFifo(), database.size(), database.size());
                    break;
                case 9:
                    startThread(databaseFile, new SimplePrioritySjf(), database.size(), database.size());
                    break;
                case 10:
                    startThread(databaseFile, new RoundRobin(QUANTUM), database.size(), time);
                    break;

            }
        } while (choose != 0);

        input.close();
    }

    private static void menu() {
        System.out.println("\nChoose the method: \n1 - FIFO\n2 - SJF\n"
                + "3 - Priority FIFO\n4 - Priority SJF\n5 - Round Robin\n" +
                "6 - FIFO Thread\n7 - SJF Thread\n8 - Priority FIFO Thread\n" +
                "9 - Priority SJF Thread\n10 - Round Robin Thread\n0 - END");
    }

    private static void startMethod(File databaseFile, Method method) throws IOException {

        List<Client> database = FileConverter.getClientData(databaseFile);
        int size = database.size();
        int finalized = method.start(database, START, END);

        System.out.println("\n\n============================================================\n"
                + "With " + method.getName() + " you can answer " + finalized +
                " clients from " + size + "\nResponse Time: " + df.format(method.getResponseTime()) +
                "\nReturn Time: " + df.format(method.getReturnTime()) +
                "\n============================================================\n");
    }

    private static void startThread(File databaseFile, Method method, int size, int op) {

        int finalized = method.startThread(databaseFile, START, END, size, op);

        System.out.println("\n\n============================================================\n"
                + "With " + method.getName() + " Thread you can answer " + finalized +
                " clients from " + size + "\nResponse Time: " + df.format(method.getResponseTime()) +
                "\nReturn Time: " + df.format(method.getReturnTime()) +
                "\n============================================================\n");
    }
}

