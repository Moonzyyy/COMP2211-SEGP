package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Impression {

  private LocalDateTime date;

  private String userId;

  private Boolean gender;

  private String age;

  private String income;

  private String context;

  private Double impressionCost;

  /**
   * @param input:     The incoming data used to create the Impression
   * @param formatter: The date formatter used when converting date string to DateTime object. It's
   *                   a parameter to avoid re-initializing for every object.
   */
  public Impression(String[] input, DateTimeFormatter formatter) {
    setDate(LocalDateTime.parse(input[0], formatter));
    setUserId(input[1]);
    setGender(input[2].equals("Male"));
    setAge(input[3]);
    setIncome(input[4]);
    setContext(input[5]);
    setImpressionCost(input[6]);
  }

  /**
   * @return get the date
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * @param date set the date
   */
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * @return get the user ID
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param id set the user ID
   */
  public void setUserId(String id) {
    this.userId = id;
  }

  /**
   * @return get the gender
   */
  public Boolean getGender() {
    return gender;
  }

  /**
   * @param gender set the gender
   */
  public void setGender(Boolean gender) {
    this.gender = gender;
  }

  /**
   * @return get the age
   */
  public String getAge() {
    return age;
  }

  /**
   * @param age set the age
   */
  public void setAge(String age) {
    this.age = age;
  }

  /**
   * @return get the income
   */
  public String getIncome() {
    return income;
  }

  /**
   * @param income set the income
   */
  public void setIncome(String income) {
    this.income = income;
  }

  /**
   * @return get context
   */
  public String getContext() {
    return context;
  }

  /**
   * @param context set the context
   */
  public void setContext(String context) {
    this.context = context;
  }

  /**
   * @return get the impression cost
   */
  public Double getImpressionCost() {
    return impressionCost;
  }

  /**
   * @param impressionCost: string to be converted to Double.
   */
  public void setImpressionCost(String impressionCost) {
    this.impressionCost = Double.parseDouble(impressionCost);
  }

}
