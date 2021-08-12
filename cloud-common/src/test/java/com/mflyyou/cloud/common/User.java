package com.mflyyou.cloud.common;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.core.api.strategory.StrategyCardId;
import com.github.houbb.sensitive.core.api.strategory.StrategyEmail;
import com.github.houbb.sensitive.core.api.strategory.StrategyPassword;
import com.github.houbb.sensitive.core.api.strategory.StrategyPhone;
import com.google.common.base.Objects;
import com.mflyyou.cloud.common.log.strategy.DefaultSensitive;

public class User {


    //    @Sensitive(strategy = StrategyChineseName.class)
    @DefaultSensitive
    private String username;

    @Sensitive(strategy = StrategyCardId.class)
    private String idCard;

    @Sensitive(strategy = StrategyPassword.class)
    private String password;

    @Sensitive(strategy = StrategyEmail.class)
    private String email;

    @Sensitive(strategy = StrategyPhone.class)
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(username, user.username) && Objects.equal(idCard, user.idCard) && Objects.equal(password, user.password) && Objects.equal(email, user.email) && Objects.equal(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, idCard, password, email, phone);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", idCard='" + idCard + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}