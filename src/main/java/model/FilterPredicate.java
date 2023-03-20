package model;

import java.util.function.Predicate;

public class FilterPredicate {
    private boolean enabled = false;
    private Predicate<User> predicate;

    FilterPredicate(Predicate<User> predicate) {
        this.predicate = predicate;
    }

    FilterPredicate(Predicate<User> predicate, boolean enabled) {
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
