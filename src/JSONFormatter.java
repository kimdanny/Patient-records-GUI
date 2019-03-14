import java.util.List;

public class JSONFormatter {

    private ReadCSV readCSV = new ReadCSV();
    private List<Patient> patientList = readCSV.CSVtoList();
    private List<String> headerList = readCSV.getHeadersToList();
    private StringBuilder singleSB = new StringBuilder();
    private StringBuilder allSB = new StringBuilder();

    public StringBuilder singlePatientJSON(String ID){
        // Traverse the list and find matchedPatient
        Patient matchedPatient = null;
        for (Patient patient : patientList){
            if (patient.getID().equals(ID)){
                matchedPatient = patient;
                break;
            }
        }
        // Start of the JSON Format
        singleSB.append("{\n");
        singleSB.append("\t\"patient\": {\n");

        boolean first = false;
        for (String head : headerList){
            if (first){singleSB.append(",\n");}
            first = true;

            singleSB.append("\t\t\""+ head +"\"" + ": " + "\"" + matchedPatient.getValueOf(head) + "\"");

        }

        singleSB.append("\n\t}");
        singleSB.append("\n}");

        return singleSB;
    }


    public StringBuilder allPatientsJSON(){

        allSB.append("{\n");
        allSB.append("\t\"patients\": [\n");

        // outer loop
        boolean outerFirst = false;
        for (Patient currentPatient : patientList){

            if (outerFirst){allSB.append(",\n");}
            outerFirst = true;
            allSB.append("\t\t{\n");

            // inner loop
            boolean innerFirst = false;
            for (String head : headerList){

                if (innerFirst){allSB.append(",\n");}
                innerFirst = true;

                allSB.append("\t\t\t\"" + head + "\"" + ": " + "\"" + currentPatient.getValueOf(head)+ "\"");

            }
            // end of inner loop

            allSB.append("\n\t\t}");

        }
        // end of outer loop

        allSB.append("\n\t]");
        allSB.append("\n}");

        return allSB;
    }

}
