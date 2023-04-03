package model;

import java.util.function.Predicate;

public class FilterPredicate {
    private boolean enabled = false;
    private Predicate<User> predicate;

    private final String code;

    public FilterPredicate(String code, Predicate<User> predicate) {
        this.code = code;
        this.predicate = predicate;
    }

    public FilterPredicate(String code, Predicate<User> predicate, boolean enabled) {
        this.code = code;
        this.predicate = predicate;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Predicate<User> getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate<User> predicate) {
        this.predicate = predicate;
    }
}
