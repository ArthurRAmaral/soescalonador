import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static final String DATABASE_FILE = "test.txt";
    private static final LocalTime START = LocalTime.of(8, 00);
    private static final LocalTime END = LocalTime.of(17, 00);

    public static void main(String[] args) throws IOException {

        File databaseFile = new File(DATABASE_FILE);

        List<Client> database = FileConverter.getClientData(databaseFile);

        SimplePriority simplePriority = new SimplePriority();

        simplePriority.start(database, START, END);
        
        System.out.println(simplePriority.getReturnTime());
        System.out.println(simplePriority.getResponseTime());

//        for (int i = 0; i < 500; i++) {
//            System.out.println(PasswordGenerator.genereteNextPassword(Service.PAGAR_BOLETO));
//        }
    }
}
