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
        		simplePriorityMethod(databaseFile);
        		break;
        	case 2:
        		roundRobinMethod(databaseFile);
            	break;
        	}
    	} while(choose != 0);
    	
    	input.close();
    }
    
    public static void menu() {
    	System.out.println("\nChoose the method: \n1 - Priority\n2 - Round Robin\n\n0 - END");
    }
    
    public static void simplePriorityMethod(File databaseFile) throws IOException {
    	SimplePriority simplePriority = new SimplePriority();
    	int finalized;
    	
    	List<Client> database = FileConverter.getClientData(databaseFile);

    	finalized = simplePriority.start(database, START, END);
    	
		System.out.println("\n\n============================================================\n"
				+ "With simple priority you can answer " + finalized + 
				" clients from " + database.size() + "\nResponse Time: " +  df.format(simplePriority.getResponseTime()) +
				"\nReturn Time: " + df.format(simplePriority.getReturnTime()) +
				"\n============================================================\n");
    }
    
    public static void roundRobinMethod(File databaseFile) throws IOException {
    	RoundRobin roundrobin = new RoundRobin(QUANTUM);
    	int finalized;
    	
    	List<Client> database = FileConverter.getClientData(databaseFile);

    	finalized = roundrobin.start(database, START, END);

    	System.out.println("\n\n============================================================\n"
    			+ "With round robin you can answer " + finalized + 
    			" clients from " + database.size() + "\nResponse Time: " + df.format(roundrobin.getResponseTime())  +
				"\nReturn Time: " + df.format(roundrobin.getReturnTime())+
				"\n============================================================\n" );
    }
}
