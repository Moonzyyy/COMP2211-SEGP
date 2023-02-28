package csvTest;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.util.Date;

public class Users {
    @CsvBindByName(column = "Date")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Date date;

    @CsvBindByName(column = "Id")
    private String id;

    @CsvBindByName(column = "Gender")
    private String gender;

    @CsvBindByName(column = "Age")
    private String age;

    @CsvBindByName(column = "Context")
    private String context;

    @CsvBindByName(column = "Impression Cost")
    private String impressionCost;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getImpressionCost() {
        return impressionCost;
    }

    public void setImpressionCost(String impressionCost) {
        this.impressionCost = impressionCost;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + "; Gender: " + getGender() + "; Age: " + getAge() + ";";
    }
}
