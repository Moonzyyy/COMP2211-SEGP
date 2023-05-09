package model;

import java.util.function.Predicate;

public record FilterPredicate(String group, Predicate<User> predicate) {
}
