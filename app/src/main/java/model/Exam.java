package model;

import java.util.Date;

/**
 * Created by Rohit on 11/24/2017.
 */

public class Exam {

    private String testName;
    private  String total;
    private  String OMR;
    private  String PSolution;
    private String progressReport;
    private String analysis;
    private String correctkey;
    private String examid;
    private Date examdate;

    public Date getExamdate() {
        return examdate;
    }

    public void setExamdate(Date examdate) {
        this.examdate = examdate;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public String getCorrectkey() {
        return correctkey;
    }

    public void setCorrectkey(String correctkey) {
        this.correctkey = correctkey;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

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
