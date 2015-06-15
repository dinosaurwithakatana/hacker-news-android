package io.dwak.holohackernews.app.models;

public class User {
    private String userName;
    private String userCookie;
    private boolean isCurrentUser;

    public User(String userName, String userCookie, boolean isCurrentUser) {
        this.userName = userName;
        this.userCookie = userCookie;
        this.isCurrentUser = isCurrentUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCookie() {
        return userCookie;
    }

    public void setUserCookie(String userCookie) {
        this.userCookie = userCookie;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public void setIsCurrentUser(boolean isCurrentUser) {
        this.isCurrentUser = isCurrentUser;
    }

}
