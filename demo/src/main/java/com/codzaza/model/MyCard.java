package com.codzaza.model;

/**
 * Created by user on 23.07.2016.
 */
public class MyCard {

    private String id;
    private String token;

    private String brand;
    private String country;
    private String customer;
    private String funding;

    private String cvc;
    private String exp_month;
    private String exp_year;
    private String last4;
    private String number;

    private String currency;

    public MyCard(String cvc, String exp_month, String exp_year, String number) {
        this.cvc = cvc;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.number = number;
        this.currency = "usd";

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getFunding() {
        return funding;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExp_month() {
        return exp_month;
    }

    public void setExp_month(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                " number=" + number.charAt(0) + number.charAt(1) + "** **** **** **" +
                number.charAt(number.length() - 2) + number.charAt(number.length() - 1);
    }
}
