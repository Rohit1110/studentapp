package model;

import java.util.Date;

/**
 * Created by Rohit on 11/30/2017.
 */

public class GeneralNotice {
    private String message;
    private Date date;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
