package com.minichat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.minichat.helpers.ViewProfiles;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "login"),
        @UniqueConstraint(columnNames = "mail")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonView({ViewProfiles.UserView.class, ViewProfiles.MessagePost.class})
    private long id;
    @Column(name = "login", unique = true, nullable = false)
    @NotNull
    @Size(min = 3, max = 20)
    @JsonView({ViewProfiles.UserPost.class, ViewProfiles.UserView.class})
    private String login;
    @Column(name = "name")
    @NotNull
    @Size(min = 3, max = 30)
    @JsonView({ViewProfiles.MessageView.class, ViewProfiles.UserPost.class, ViewProfiles.UserView.class})
    private String name;
    @Column(name = "mail", unique = true, nullable = false)
    @NotNull
    @Pattern(regexp = "\\w+[\\-]?\\w+@\\w+[\\.{1}\\w+]+")
    @JsonView({ViewProfiles.UserPost.class, ViewProfiles.UserView.class})
    private String mail;
    @Column(name = "pwd", nullable = false)
    @NotNull
    @JsonView(ViewProfiles.UserPost.class)
    private String pwd;
    @OneToMany(mappedBy = "user")
    @OrderColumn(name = "timestamp")
    @JsonView(ViewProfiles.ViewUserMesssages.class)
    private Collection<Message> messages;

    public User() {
    }

    public User(String login, String name, String mail, String pwd) {
        this.login = login;
        this.name = name;
        this.mail = mail;
        this.pwd = pwd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getId() != user.getId()) return false;
        if (!getLogin().equals(user.getLogin())) return false;
        return getMail().equals(user.getMail());
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getLogin().hashCode();
        result = 31 * result + getMail().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
