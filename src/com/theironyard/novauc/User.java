package com.theironyard.novauc;

public class User {

    private boolean homePage = false;
    private boolean viewAllPage = false;
    private boolean search = false;
    private String userName;
    private String password;

    public User(){
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public boolean isHomePage() {
        return homePage;
    }

    public void setHomePage(boolean homePage) {
        this.homePage = homePage;
    }

    public boolean isViewAllPage() {
        return viewAllPage;
    }

    public void setViewAllPage(boolean viewAllPage) {
        this.viewAllPage = viewAllPage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }
}
