package model;

import java.util.function.Predicate;

public class FilterPredicate {
    private final Predicate<User> predicate;
    private final String group;

    public FilterPredicate(String group, Predicate<User> predicate) {
        this.group = group;
        this.predicate = predicate;
    }

    public Predicate<User> getPredicate() {
        return predicate;
    }

    public String getGroup() {
        return group;
    }
}
