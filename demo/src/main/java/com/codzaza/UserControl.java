package com.codzaza;

import com.codzaza.model.User;

import java.util.ArrayList;

/**
 * Created by user on 25.07.2016.
 */
public class UserControl {

    static public Integer FindUser(ArrayList<User> Users, String email){
        if (Users.size() == 0){
            return -1;
        }
        for (Integer i = 0; i < Users.size(); ++i){
            if (Users.get(i).getEmail().equals(email)){
                return i;
            }
        }
        return -1;
    }

    static public Boolean CheckUser(User user, String password){
        return user.getPassword().equals(password);
    }

    static public Boolean CheckUser(ArrayList<User> Users, Integer key, String password){
        return Users.get(key).getPassword().equals(password);
    }

    static public Boolean CheckUser(ArrayList<User> Users, String email, String password){
        return Users.get(UserControl.FindUser(Users, email)).getPassword().equals(password);
    }

}
