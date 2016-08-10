package com.codzaza.API;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.*;

import java.util.*;

/**
 * Created by user on 28.07.2016.
 */
public class Management {

    private static final String SECRET_KEY = "sk_test_f1adJsfLd0eWYP84RQU3Qcw8";
    //private Stripe stripe;
    private static Boolean isAuthorized = false;

    public static void Authorization(){
        Stripe.apiKey = SECRET_KEY;
        isAuthorized = true;
    }

//    public Management(String key) {
//        stripe.apiKey = key;
//    }


    public static Boolean getIsAuthorized() {
        return isAuthorized;
    }

    public static String CreateAccount(String country, String email) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        HashMap<String, Object> acc_param = new HashMap<String, Object>();
        acc_param.put("country", country);
        acc_param.put("managed", true);
        //acc_param.put("email", email);
        return Account.create(acc_param).getId();
    }

    public static String CreateCardToken(String number, String cvc, String exp_month, String exp_year, String currency) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        HashMap<String, Object> cardParam = new HashMap<String, Object>();
        HashMap<String, Object> tokenParam = new HashMap<String, Object>();
        cardParam.put("number", number);
        cardParam.put("cvc", cvc);
        cardParam.put("exp_month", Integer.parseInt(exp_month));
        cardParam.put("exp_year", Integer.parseInt(exp_year));
        cardParam.put("currency", currency);
        tokenParam.put("card", cardParam);
        return Token.create(tokenParam).getId();
    }

    public static ExternalAccount CreateCard(String account_id, String number, String cvc, String exp_month, String exp_year, String currency) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> params = new HashMap<String, Object>();
        Account account = Account.retrieve(account_id);
        String token_id = CreateCardToken(number, cvc, exp_month, exp_year, currency);
        params.put("external_account", token_id);
        return account.getExternalAccounts().create(params);
    }

    public static ExternalAccount CreateCard(String token_id, String account_id) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> params = new HashMap<String, Object>();
        Account account = Account.retrieve(account_id);
        params.put("external_account", token_id);
        return account.getExternalAccounts().create(params);
    }

    public static String CreateCustomer(String firstName, String secondName, String email, String country, String city, String line1, String currency) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        HashMap<String, Object> custParam = new HashMap<String, Object>();
        HashMap<String, Object> shippingParam = new HashMap<String, Object>();
        HashMap<String, Object> addressParam = new HashMap<String, Object>();
        addressParam.put("country", country);
        addressParam.put("city", city);
        addressParam.put("line1", line1);
        shippingParam.put("address", addressParam);
        shippingParam.put("name", firstName + secondName);
        custParam.put("email", email);
        custParam.put("shipping", shippingParam);
        custParam.put("account_balance", 0);
        return Customer.create(custParam).getId();
    }

    public static String AddSourcesToCustomer(String custId, String tokkenId) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {

        Customer customer = Customer.retrieve(custId);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", tokkenId);
        ExternalAccount externalAccount = customer.getSources().create(params);

        return externalAccount.getId();
    }

    public static String getCardId(String custId, Integer cardNum) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Customer customer = Customer.retrieve(custId);
        List<ExternalAccount> sources = customer.getSources().getData();
        Account account = Account.retrieve(sources.get(cardNum).getId());
        return account.getId();
    }

    public static void DeleteCustomer(String custId) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Customer customer = Customer.retrieve(custId);
        customer.delete();
    }

    public static void DoCharge(String custId, String sourceId, Integer amount, String destinationId, String currency) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {

        System.out.println("<-----StartCharge----->\n" + Customer.retrieve(custId).getSources().toString());

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("source", sourceId);
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("customer", custId);

        chargeParams.put("destination", CreateAccount("US", ""));

        System.out.println("<-----AddChargeParam----->\n" + chargeParams.toString());

        Charge charge = Charge.create(chargeParams);

        System.out.println("<-----FinishCharge----->\n" + charge.toString());
    }

    public static Integer GetBalance(String custId) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {

        System.out.println("<-----GetBalance----->");

        return Customer.retrieve(custId).getAccountBalance();
    }

}
