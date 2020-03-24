import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class Main {
	
	public static final String DATABASE_FILE = "test.txt";
	public static final LocalTime QUANTUM = LocalTime.of(0, 1);
	private static final LocalTime START = LocalTime.of(8, 00);
	private static final LocalTime END = LocalTime.of(17, 00);
	
    public static void main(String[] args) throws IOException {

    	File databaseFile = new File(DATABASE_FILE);

    	List<Client> database = FileConverter.getClientData(databaseFile);
    	
    	int finalized = RoundRobin.start(database, QUANTUM, START, END);
    	
    	System.out.println("\nWith round robin you can answer " + finalized + " clients from " + database.size() + " in one day and end at " + RoundRobin.getActual());
//        for (int i = 0; i < 500; i++) {
//            System.out.println(PasswordGenerator.genereteNextPassword(Service.PAGAR_BOLETO));
//        }
    }
}
