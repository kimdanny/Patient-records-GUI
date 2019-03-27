import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * <Reference>
 * 1. private method 'mostCommonOne' is inspired by arshajii's answer.
 *   - Arshajii (2013) 'Java-get most common element in a list', Available at: https://stackoverflow.com/questions/19031213/java-get-most-common-element-in-a-list
 *
 */


public class Model {
    private ReadCSV readCSV = new ReadCSV();
    private List<Patient> patientList;
    //private List<String> headerList;

    private JSONFormatter jsonFormatter;
    private StringBuilder allPatientsJSON;


    // Read an input file and generate Patient objects
    public List<Patient> readFile(String filepath){
        return readCSV.CSVtoList(filepath);
    }

    // Constructor that links file info to patientList and jsonFormatter
    public Model(String filepath){
        patientList = this.readFile(filepath);
        //headerList = this.readCSV.getHeadersToList(filepath);
        jsonFormatter = new JSONFormatter(filepath);
        allPatientsJSON = jsonFormatter.allPatientsJSON();
    }

    /******************
     * Getter methods *
     ******************/
    protected StringBuilder getAllPatientJSON(){
        return allPatientsJSON;
    }

    protected StringBuilder getSinglePatientJSONbyID(String MustBeID){
        return jsonFormatter.singlePatientJSONbyID(MustBeID);
    }

    protected StringBuilder getSinglePatientJSONbyName(String first, String last){
        return jsonFormatter.singlePatientJSONbyName(first, last);
    }

    protected List<String> getFirstNamesOfAllPatient(){
        List<String> nameList = new ArrayList<>();
        for (Patient patient : patientList){
            nameList.add(patient.getFIRST());
        }
        return nameList;
    }

    protected List<String> getIDOfAllPatient(){
        List<String> idList = new ArrayList<>();
        for (Patient patient : patientList){
            idList.add(patient.getID());
        }
        return idList;
    }

    protected List<String> getDeadPatients(){
        List<String> deadList = new ArrayList<>();
        for (Patient patient : patientList){
            if (patient.getDEATHDATE() != ""){
                deadList.add(patient.getDEATHDATE());
            }
        }
        return deadList;
    }

    protected String getAverageAge(){

        List<String> ageList = new ArrayList<>();

        for (Patient patient : patientList){
            String birthDate = patient.getBIRTHDATE();

        }

        return "";
    }

    protected String getMostCommonYearOfBirth(){
        List<String> yearsList = new ArrayList<>();

        for (Patient patient : patientList){
            String birthDate = patient.getBIRTHDATE();
            String[] result = birthDate.split("-");
            yearsList.add(result[0]);
        }
        return mostCommonOne(yearsList);
    }

    // helper class for getMostCommonYearOfBirth()
    private String mostCommonOne(List<String> inputList) {

        Map<String, Integer> map = new HashMap<>();
        String resultKey = null;
        int tempNumber = 0;

        for(int x = 0; x < inputList.size(); x++) {

            Integer howMany = map.get(inputList.get(x));
            if(howMany == null) {
                map.put(inputList.get(x), 1);
            }
            else {
                map.put(inputList.get(x), howMany+1);
            }
        }

        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            if(entry.getValue() > tempNumber) {
                resultKey = entry.getKey();
                tempNumber = entry.getValue();
            }
        }
        return resultKey;
    }


    /**
     * Search methods
     * ID is a distinct value, so if matched -> break
     * @param ID
     * @return Patient type
     */
    protected Patient searchByID(String ID){
        // traversing
        Patient matchedPatient = null;
        for (Patient patient : patientList){
            if (patient.getID().equals(ID)){
                matchedPatient = patient;
                break;
            }
        }
        return matchedPatient;
    }

    /**
     * Search methods
     * First name is not a distinct value, so no breaking
     * @param First
     * @return Patient type
     */
    protected List<Patient> searchByFirst(String First){
        List<Patient> result = new ArrayList<>();
        // traversing
        for (Patient patient : patientList){
            if (patient.getFIRST().equals(First)){
                result.add(patient);
            }
        }
        return result;
    }

    /**
     * Search methods
     * Last name is not a distinct value, so no breaking
     * @param Last
     * @return Patient type
     */
    protected List<Patient> searchByLast(String Last){
        List<Patient> result = new ArrayList<>();
        // traversing
        for (Patient patient : patientList){
            if (patient.getLAST().equals(Last)){
                result.add(patient);
            }
        }
        return result;
    }

}
