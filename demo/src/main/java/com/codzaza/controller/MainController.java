package com.codzaza.controller;

import com.codzaza.API.Management;
import com.codzaza.UserControl;
import com.codzaza.model.MyCard;
import com.codzaza.model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by user on 18.07.2016.
 */
@Controller
public class MainController {

    private ArrayList<User> Users = new ArrayList<User>() ;// = new List<User>();
    private final String SECRET_KEY = "sk_test_f1adJsfLd0eWYP84RQU3Qcw8";

    @RequestMapping(value = "/", method = RequestMethod.GET)
     public String StartPage() {
        //System.out.println("start");
        return "index";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String SingInPage() {
        return "signup";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String ButtonPage() {

        return "button_page";
    }

    @RequestMapping(value = "/registr")
    public ModelAndView SingInPage(HttpServletRequest myrequest) {
        ModelAndView mav = new ModelAndView();
        Integer current = 0;
        String password = myrequest.getParameter("password");
        try {
            if (password.equals(myrequest.getParameter("repeat"))) {
                if (UserControl.FindUser(Users, myrequest.getParameter("email")) < 0) {
                    Users.add(new User(myrequest.getParameter("first_name"), myrequest.getParameter("second_name"),
                            myrequest.getParameter("country"), myrequest.getParameter("city"),
                            myrequest.getParameter("line1"),  myrequest.getParameter("email"),
                            "cust_" + RandomStringUtils.randomAlphanumeric(16), password));
                    current = Users.size() - 1;
                } else throw (new Exception("Email already is used"));

                if (!Management.getIsAuthorized()){
                    Management.Authorization();
                }

                String firstName = Users.get(current).getFirstname();
                String secondName = Users.get(current).getSecondname();
                String email = Users.get(current).getEmail();
                String country = Users.get(current).getCountry();
                String city = Users.get(current).getCity();
                String line1 = Users.get(current).getLine1();

                Double amount = Users.get(current).getBalance() * 0.01;

                Users.get(current).setId(Management.CreateCustomer(firstName, secondName, email, country, city, line1, "usd"));
                mav.addObject("user_name", Users.get(current).getFirstname() + Users.get(current).getSecondname());
                mav.addObject("user_firstname", Users.get(current).getFirstname());
                mav.addObject("user_secondname", Users.get(current).getSecondname());
                mav.addObject("user_email", Users.get(current).getEmail());
                mav.addObject("user_card", "here will be card info");
                mav.addObject("user_id", Users.get(current).getId());
                mav.addObject("user_amount", amount.toString() + '$');
                mav.setViewName("user_account");
            } else throw (new Exception("Diferent passwords"));
        }
        catch (Exception ex){
            mav.setViewName("error_report");
            mav.addObject("error_message", "Sign Up error: " + ex.getMessage());//ex.getClass().toString() + " " + ex.getMessage());
            if (current == Users.size() - 1){
                Users.remove(current);
            }
        }
        return mav;
    }

    @RequestMapping(value = "/delete")
    public ModelAndView DeleteCustomer(HttpServletRequest myrequest) {
//        for (Iterator<User> i = Users.iterator(); i.hasNext();) {
//            User user = i.next();
//            if (user.getId() == (myrequest.getParameter("id"))) {
//                Users.remove(user);
//                System.out.println("delete");
//                break;
//            }
//        }
        ModelAndView mav = new ModelAndView();
        try{
            if (!Management.getIsAuthorized()){
                Management.Authorization();
            }
            Management.DeleteCustomer(Users.get(Users.size() - 1).getId());
            Users.remove(Users.size() - 1);
            System.out.println("delete");
            mav.setViewName("index");
        }
        catch (Exception ex){
            mav.setViewName("error_report");
            mav.addObject("error_message", "Sign Up error: " + ex.getMessage());//ex.getClass().toString() + " " + ex.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/add_card")
    public ModelAndView AddCardPage() {
        ModelAndView mav = new ModelAndView("/add_card_account");
        return mav;
    }

    @RequestMapping(value = "/registr_card")
    public ModelAndView RegitCard(HttpServletRequest myrequest){
        ModelAndView mav = new ModelAndView();

        try {
            int current = Users.size() - 1;
            Double amount = Users.get(current).getBalance() * 0.01;
            MyCard myCard = new MyCard(myrequest.getParameter("cvc"), myrequest.getParameter("exp_month"), myrequest.getParameter("exp_year"),
                    myrequest.getParameter("number"));
            Users.get(current).AddCard(myCard);
            mav.addObject("user_name", Users.get(current).getFirstname() + Users.get(current).getSecondname());
            mav.addObject("user_firstname", Users.get(current).getFirstname());
            mav.addObject("user_secondname", Users.get(current).getSecondname());
            mav.addObject("user_email", Users.get(current).getEmail());
            mav.addObject("user_card", Users.get(current).getCards());
            mav.addObject("user_id", Users.get(current).getId());
            mav.addObject("user_amount", amount.toString() + '$');
            mav.setViewName("user_account");
        }
        catch (Exception ex) {
            mav.setViewName("error_report");
            mav.addObject("error_message", "Sign Up error: " + ex.getMessage());//ex.getClass().toString() + " " + ex.getMessage());
        }
        return mav;
    }

    @RequestMapping(value = "/charge_info")
    public ModelAndView ChargeInfo(HttpServletRequest myrequest){
        ModelAndView mav = new ModelAndView("charge_card");
        return mav;
    }

    @RequestMapping(value = "/charge_card")
    public ModelAndView ChargeCard(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        try{
            Integer current = Users.size() - 1;

            Users.get(current).CardCharge(request.getParameter("card"),new MyCard(request.getParameter("cvc"), request.getParameter("exp_month"), request.getParameter("exp_year"),
                    request.getParameter("number")), Integer.parseInt(request.getParameter("amount")));

            Double amount = Users.get(current).getBalance() * 0.01;
            mav.setViewName("user_account");
            mav.addObject("user_name", Users.get(current).getFirstname() + Users.get(current).getSecondname());
            mav.addObject("user_firstname", Users.get(current).getFirstname());
            mav.addObject("user_secondname", Users.get(current).getSecondname());
            mav.addObject("user_email", Users.get(current).getEmail());
            mav.addObject("user_card", "here will be card info");
            mav.addObject("user_id", Users.get(current).getId());
            mav.addObject("user_amount", amount.toString() + '$');
        }
        catch (Exception ex){
            mav.setViewName("error_report");
            mav.addObject("error_message", "Sign Up error: " + ex.getMessage());
        }
        return mav;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException exception) {
        ModelAndView modelAndView = new ModelAndView("error_report");
        modelAndView.addObject("error_message", exception.getMessage());
        return modelAndView;
    }

//    @RequestMapping(value = "/error")
//    public ModelAndView ErrorPage(HttpServletRequest request) {
//        ModelAndView modelAndView = new ModelAndView("error_report");
//        modelAndView.addObject("error_message", "error");//exception.getMessage());
//        return modelAndView;
//    }

    @RequestMapping(value = "/string", method = RequestMethod.GET)
    public @ResponseBody String stringResponse() {

        return "Some string";
    }


}
