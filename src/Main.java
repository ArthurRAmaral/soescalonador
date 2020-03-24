import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
	
	public static final String DATABASE_FILE = "test.txt";
	
    public static void main(String[] args) throws IOException {

    	File databaseFile = new File(DATABASE_FILE);

    	List<Client> database = FileConverter.getClientData(databaseFile);
    	System.out.println(database);
    	
//        for (int i = 0; i < 500; i++) {
//            System.out.println(PasswordGenerator.genereteNextPassword(Service.PAGAR_BOLETO));
//        }
    }
}
