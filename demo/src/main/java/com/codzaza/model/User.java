package com.codzaza.model;

import com.codzaza.API.Management;
import com.stripe.exception.*;

import java.util.*;

/**
 * Created by user on 22.07.2016.
 */
public class User {

    private String firstname;
    private String secondname;

    private String country;
    private String city;
    private String line1;
    private String email;

    private String id;
    private String password;
    private String account;

    private Integer balance;

    private ArrayList<MyCard> user_My_cards;

    public User(String firstname, String secondname, String country, String city, String line1, String email, String id, String password) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        this.firstname = firstname;
        this.secondname = secondname;
        this.country = country;
        this.city = city;
        this.line1 = line1;
        this.email = email;
        this.id = id;
        this.password = password;
        this.balance = 0;
        this.user_My_cards = new ArrayList<MyCard>();
        if (!Management.getIsAuthorized()){
            Management.Authorization();
        }
        this.account = Management.CreateAccount(country, email);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public void AddCard(MyCard myCard) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException, Exception {
        for (Iterator<MyCard> iterator = user_My_cards.iterator(); iterator.hasNext();){
            if (iterator.next().getNumber().equals(myCard.getNumber())) throw (new Exception("Incorrect card data"));
        }
        if (!Management.getIsAuthorized()){
            Management.Authorization();
        }
        myCard.setToken(Management.CreateCardToken(myCard.getNumber(), myCard.getCvc(), myCard.getExp_month(), myCard.getExp_year(), myCard.getCurrency()));
        myCard.setId(Management.AddSourcesToCustomer(id, myCard.getToken()));
        user_My_cards.add(myCard);

    }

    public String getCards(){
        String str = "";
        for (Iterator<MyCard> iter = user_My_cards.iterator(); iter.hasNext();){
            str += iter.next().toString() + " ";
        }
        return str;
    }

    public String CardCharge(String number, MyCard myCard, Integer amount) throws Exception {
        if (number.equals(myCard.getNumber())) throw (new Exception("Incorrect charge data (cards number have to be different)"));
        Integer cardNumber = -1;
        for (Integer i = 0; i < user_My_cards.size(); ++i){
            if (user_My_cards.get(i).getNumber().equals(number)){
                cardNumber = i;
            }
        }
        if (cardNumber < 0) throw (new Exception("Incorrect charge data (no source)"));
        if (!Management.getIsAuthorized()){
            Management.Authorization();
        }
        Management.DoCharge(id, user_My_cards.get(cardNumber).getId(), amount,
                "Management.CreateCardToken(myCard.getNumber(), myCard.getCvc(), myCard.getExp_month(), myCard.getExp_year(), myCard.getCurrency())", "usd");
        balance = Management.GetBalance(id);
        return "";
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", user_My_cards=" + user_My_cards.toString() +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", secondname='" + secondname + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }
}
