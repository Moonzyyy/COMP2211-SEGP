package core;


import model.User;
import one.microstream.reference.Lazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataRoot
{
    private final String name;
    private Lazy<HashMap<Long, User>> users;

    public DataRoot(String name) {
        this.name = name;
        this.users = Lazy.Reference(new HashMap<>());
    }

    public String getName() {
        return this.name;
    }

    public HashMap<Long, User> getUsers() {
        return Lazy.get(users);
    }

    public void setUsers(Lazy<HashMap<Long, User>> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Root: " + this.name;
    }
}