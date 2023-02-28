package csvTest;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDateTime;

public class Impression {
    private LocalDateTime date;

    private String userId;

    private String gender;

    private String age;

    private String context;

    private String impressionCost;

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

//    @Override
//    public String toString() {
//        return "ID: " + getId() + "; Gender: " + getGender() + "; Age: " + getAge() + ";";
//    }
}
