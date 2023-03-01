package csvTest;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Impression {
    private LocalDateTime date;

    private String userId;

    private String gender;

    private String age;

    private String income;

    private String context;

    private Double impressionCost;

    public Impression(String[] input, DateTimeFormatter formatter) {
        setDate(LocalDateTime.parse(input[0], formatter));
        setUserId(input[1]);
        setGender(input[2]);
        setAge(input[3]);
        setIncome(input[4]);
        setContext(input[5]);
        setImpressionCost(input[6]);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = userId;
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

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Double getImpressionCost() {
        return impressionCost;
    }

    public void setImpressionCost(String impressionCost) {
        this.impressionCost = Double.parseDouble(impressionCost);
    }

//    @Override
//    public String toString() {
//        return "ID: " + getId() + "; Gender: " + getGender() + "; Age: " + getAge() + ";";
//    }
}
