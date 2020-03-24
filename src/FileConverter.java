import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileConverter {

    public static List<Client> getClientData(File file) throws IOException {
        List<Client> returnList = new ArrayList<Client>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        while (line != null) {

            String values[] = line.split(";");

            String code = values[0];
            String cpf = values[1].substring(0, 9) + "-" + values[1].substring(9);
            int priority = Integer.parseInt(values[2]);

            String[] time = values[3].split(":");
            LocalTime estimatedTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));

            time = values[4].split(":");
            LocalTime arrivalTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));

            Client tmpClient = new Client(code, cpf, priority, estimatedTime, arrivalTime);
            returnList.add(tmpClient);

            line = br.readLine();
        }

        br.close();

        return returnList;
    }
}
