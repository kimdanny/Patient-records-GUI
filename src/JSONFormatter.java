import java.io.IOException;
import java.util.List;

public class JSONFormatter {

    private ReadCSV readCSV = new ReadCSV();
    private ReadJSON readJSON = new ReadJSON();
    private List<Patient> patientList;
    private List<String> headerList;
    private StringBuilder singleSBbyID = new StringBuilder();
    private StringBuilder singleSBbyName = new StringBuilder();
    private StringBuilder allSB = new StringBuilder();

    // Read an input file and generate Patient objects
    private List<Patient> readCSVfile(String filepath){
        return readCSV.CSVtoList(filepath);
    }
    private List<Patient> readJSONfile(String filepate) throws Exception {
            return readJSON.JSONtoList(filepate);
    }

    // Constructor that links file info to patientList and headerList
    public JSONFormatter(String filepath) throws Exception {
        patientList = (filepath.endsWith(".csv")) ? this.readCSVfile(filepath) : this.readJSONfile(filepath);
        headerList = (filepath.endsWith(".csv")) ? this.readCSV.getHeadersToList(filepath) : this.readJSON.getHeadersToList(filepath);
    }

    public StringBuilder singlePatientJSONbyID(String ID){
        // Traverse the list and find matchedPatient
        Patient matchedPatient = null;
        for (Patient patient : patientList){
            if (patient.getID().equals(ID)){
                matchedPatient = patient;
                break;
            }
        }
        // Start of the JSON Format
        singleSBbyID.append("{\n");
        singleSBbyID.append("\t\"patient\": {\n");

        boolean first = false;
        for (String head : headerList){
            if (first){
                singleSBbyID.append(",\n");}
            first = true;

            singleSBbyID.append("\t\t\""+ head +"\"" + ": " + "\"" + matchedPatient.getValueOf(head) + "\"");

        }

        singleSBbyID.append("\n\t}");
        singleSBbyID.append("\n}");

        return singleSBbyID;
    }

    public StringBuilder singlePatientJSONbyName(String First, String Last){
        Patient matchedPatient = null;
        for (Patient patient : patientList){
            if (patient.getFIRST().equals(First) && patient.getLAST().equals(Last)){
                matchedPatient = patient;
                break;
            }
        }
        // Start JSON Format
        singleSBbyName.append("{\n");
        singleSBbyName.append("\t\"patient\": {\n");

        boolean first = false;
        for (String head : headerList){
            if (first){
                singleSBbyName.append(",\n");}
            first = true;

            singleSBbyName.append("\t\t\""+ head +"\"" + ": " + "\"" + matchedPatient.getValueOf(head) + "\"");
        }

        singleSBbyName.append("\n\t}");
        singleSBbyName.append("\n}");

        return singleSBbyName;


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
