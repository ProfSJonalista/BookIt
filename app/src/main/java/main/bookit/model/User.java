package main.bookit.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {

    private String email;
    private List<UserBook> userBookList;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email) {
        this.email = email;
        this.userBookList = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserBook> getUserBookList() {
        return userBookList;
    }

    public void setUserBookList(List<UserBook> userBookList) {
        this.userBookList = userBookList;
    }
}
