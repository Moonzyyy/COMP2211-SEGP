package model;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.util.Pair;
public class User {

  /**
   * A list of pairs of date and impression cost associated with this user.
   */
  private final ArrayList<Pair<LocalDateTime, Double>> impressions;
  /**
   * A list of pairs of a click date and click cost associated with this user.
   */
  private final ArrayList<Pair<LocalDateTime, Double>> clicks;
  private final ArrayList<Server> servers;
  private Long id;
  private Boolean gender;
  private Age age;
  private Context context;
  private Income income;

  /**
   * Every unique user, gathered by ID, with their attributes,
   * including clicks, impressions and servers
   */
  public User(String[] input) {
    impressions = new ArrayList<>();
    clicks = new ArrayList<>();
    servers = new ArrayList<>();

    setId(input[1]);
    setGender(input[2].equals("Male"));
    setAge(input[3]);
    setIncome(input[4]);
    setContext(input[5]);
  }

  public Long getId() {
    return id;
  }

  public void setId(String id) {
    this.id = Long.parseLong(id);
  }

  public Boolean getGender() {
    return gender;
  }

  public void setGender(Boolean gender) {
    this.gender = gender;
  }

  public Age getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = Age.valueOfLabel(age);
  }

  public void setIncome(String income) {
    this.income = Income.valueOf(income.toUpperCase());
  }

  public Income getIncome() {
    return income;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = Context.valueOfLabel(context);
  }

  public void addImpression(Pair<LocalDateTime, Double> impression) {
    impressions.add(impression);
  }

  public void addClick(Pair<LocalDateTime, Double> click) {
    this.clicks.add(click);
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

  public ArrayList<Pair<LocalDateTime, Double>> getClicks() {
    return clicks;
  }

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//
//    User that = (User) o;
//
//    return id.equals(that.id);
//  }
//
//  @Override
//  public int hashCode() {
//    return Long.hashCode(id);
//  }
}