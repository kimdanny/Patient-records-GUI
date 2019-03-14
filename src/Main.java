import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String args[]) throws FileNotFoundException {

            ReadCSV readCSV = new ReadCSV();
//        List<Patient> a = readCSV.CSVtoList();
//        System.out.println(a);
//        System.out.println(a.toArray().length);
        //System.out.println(readCSV.getHeaders());

        JSONFormatter jsonFormatter = new JSONFormatter();
        //System.out.println( jsonFormatter.singlePatientJSON("17e1a347-8e15-4f6c-bf9c-7d7b717cfc73") );
        System.out.println(jsonFormatter.allPatientsJSON());

    }
}
