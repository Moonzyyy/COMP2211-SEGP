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


  /**
   * Male = True
   * Female = False
   */
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

  /**
   * @return get the ID
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id set the ID
   */
  public void setId(String id) {
    this.id = Long.parseLong(id);
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
  public Age getAge() {
    return age;
  }

  /**
   * @param age set the age
   */
  public void setAge(String age) {
    this.age = Age.valueOfLabel(age);
  }

  /**
   * @param income set the income
   */
  public void setIncome(String income) {
    this.income = Income.valueOf(income.toUpperCase());
  }

  /**
   * @return get the income
   */
  public Income getIncome() {
    return income;
  }

  /**
   * @return get the context
   */
  public Context getContext() {
    return context;
  }

  /**
   * @param context set the context
   */
  public void setContext(String context) {
    this.context = Context.valueOfLabel(context);
  }

  /**
   * @param impression add the impression into the list of impressions
   */
  public void addImpression(Pair<LocalDateTime, Double> impression) {
    impressions.add(impression);
  }

  /**
   * @param click add the click into the list of clicks
   */
  public void addClick(Pair<LocalDateTime, Double> click) {
    this.clicks.add(click);
  }

  /**
   * @param server add the server to the list of servers
   */
  public void addServer(Server server) {
    servers.add(server);
  }

  /**
   * @return get the list of servers
   */
  public ArrayList<Server> getServers() {
    return servers;
  }

  /**
   * @return get the list of impressions
   */
  public ArrayList<Pair<LocalDateTime, Double>> getImpressions() {
    return impressions;
  }

  /**
   * @return get the list of clicks
   */
  public ArrayList<Pair<LocalDateTime, Double>> getClicks() {
    return clicks;
  }


}