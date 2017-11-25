package model;

/**
 * Created by Rohit on 11/24/2017.
 */

public class Exam {

    private String testName;
    private  String total;
    private  String OMR;
    private  String PSolution;
    private String progressReport;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOMR() {
        return OMR;
    }

    public void setOMR(String OMR) {
        this.OMR = OMR;
    }

    public String getPSolution() {
        return PSolution;
    }

    public void setPSolution(String PSolution) {
        this.PSolution = PSolution;
    }

    public String getProgressReport() {
        return progressReport;
    }

    public void setProgressReport(String progressReport) {
        this.progressReport = progressReport;
    }
}
