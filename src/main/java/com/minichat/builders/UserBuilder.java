package com.minichat.builders;

import com.minichat.models.User;

public class UserBuilder {
    private long id = 1;
    private String login = "login1";
    private String name = "name1";
    private String mail = "testmail@mail.ru";
    private String pwd = "1111";

    public UserBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withMail(String mail) {
        this.mail = mail;
        return this;
    }

    public UserBuilder withPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public User build() {
        User user = new User(login, name, mail, pwd);
        user.setId(id);
        return user;
    }
}
