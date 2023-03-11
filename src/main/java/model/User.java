package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.util.Pair;
import model.segments.Age;

import static model.segments.Age.*;

public class User {

  /**
   * A list of pairs of date and impression cost associated with this user.
   */
  private final ArrayList<Pair<LocalDateTime, Double>> impressions;
  /**
   * A list of pairs of a click date and click cost associated with this user.
   */
  private final ArrayList<Pair<LocalDateTime, Integer>> click;
  private final ArrayList<Server> servers;
  private Long id;
  private String gender;
  private Age age;
  private String context;
  private String income;

  public User(String[] input, DateTimeFormatter formatter) {
    impressions = new ArrayList<>();
    click = new ArrayList<>();
    servers = new ArrayList<>();

    setId(input[1]);
    setGender(input[2]);
    setAge(input[3]);
    setIncome(input[4]);
    setContext(input[5]);
//    addImpression(
//        new Pair<>(LocalDateTime.parse(input[0], formatter), Double.parseDouble(input[6])));
  }

  public Long getId() {
    return id;
  }

  public void setId(String id) {
    this.id = Long.parseLong(id);
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Age getAge() {
    return age;
  }

  public void setAge(String age) {
    switch (age) {
      case "<25" -> this.age = U25;
      case "25-34" -> this.age = U34;
      case "35-44" -> this.age = U44;
      case "45-54" -> this.age = U54;
      default -> this.age = O54;
    }
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

  public void addImpression(Pair<LocalDateTime, Double> impression) {
    impressions.add(impression);
  }

  public void addClick(Pair<LocalDateTime, Integer> click) {
    this.click.add(click);
  }

  public void addServer(Server server) {
    servers.add(server);
  }

  public ArrayList<Server> getServers() {
    return servers;
  }

  public ArrayList<Pair<LocalDateTime, Double>> getImpressions() {
    return impressions;
  }

  public ArrayList<Pair<LocalDateTime, Integer>> getClicks() {
    return click;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User that = (User) o;

    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }
}