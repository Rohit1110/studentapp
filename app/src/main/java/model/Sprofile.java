package model;

/**
 * Created by Rohit on 11/24/2017.
 */

public class Sprofile {
    private  String StudentContact;
    private  String Category;
    private  String PrePercent;
    private  String ParentContact;



    private  String studclass;
    private  String RollNo;
    private  String Name;
    private  String Div;



    public Sprofile() {
    }

    public Sprofile(String studentContact, String category, String prePercent, String parentContact, String studclass1, String rollNo, String name, String div) {
        StudentContact = studentContact;
        Category = category;
        PrePercent = prePercent;
        ParentContact = parentContact;
        studclass = studclass1;
        RollNo = rollNo;
        Name = name;
        Div = div;

    }

    public String getStudentContact() {
        return StudentContact;
    }

    public String getCategory() {
        return Category;
    }

    public String getPrePercent() {
        return PrePercent;
    }

    public String getParentContact() {
        return ParentContact;
    }


    public String getStudclass() {
        return studclass;
    }

    public String getRollNo() {
        return RollNo;
    }

    public String getName() {
        return Name;
    }

    public String getDiv() {
        return Div;
    }

}
