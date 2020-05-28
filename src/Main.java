import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	public static final String DATABASE_FILE = "test.txt";
	public static final LocalTime QUANTUM = LocalTime.of(0, 1);
	private static final LocalTime START = LocalTime.of(8, 00);
	private static final LocalTime END = LocalTime.of(17, 00);
	private static DecimalFormat df =  new DecimalFormat("0.00");

	
    public static void main(String[] args) throws IOException {

    	Scanner input = new Scanner(System.in);
        File databaseFile = new File(DATABASE_FILE);
    	
    	int choose = 0;
    	
    	do {
        	menu();
        	choose = input.nextInt();
        	
        	switch(choose) {
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
        	}
    	} while(choose != 0);
    	
    	input.close();
    }
    
    public static void menu() {
    	System.out.println("\nChoose the method: \n1 - FIFO\n2 - SJF\n"
    			+ "3 - Priority FIFO\n4 - Priority SJF\n5 - Round Robin\n0 - END");
    }
    
    public static void startMethod(File databaseFile, Method method) throws IOException {
    	
    	List<Client> database = FileConverter.getClientData(databaseFile);
    	int finalized = method.start(database, START, END);
    	
    	System.out.println("\n\n============================================================\n"
				+ "With " + method.getName() +  " you can answer " + finalized + 
				" clients from " + database.size() + "\nResponse Time: " +  df.format(method.getResponseTime()) +
				"\nReturn Time: " + df.format(method.getReturnTime()) +
				"\n============================================================\n");
    }
}

