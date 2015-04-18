package com.acipi.evote.db.service;

import android.content.Context;

import com.acipi.evote.db.dao.UserDAO;

import java.util.HashMap;

/**
 * Created by Altin Cipi on 2/7/2015.
 */
public class UserService
{
    private UserDAO userDAO;

    public UserService(Context context)
    {
        userDAO = new UserDAO(context);
    }

    public boolean isUserLoggedIn()
    {
        int count = userDAO.getRowCount();
        if (count > 0)
        {
            return true;
        }
        return false;
    }

    public boolean logoutUser()
    {
        userDAO.resetTables();
        return true;
    }

    public void addUser(String username, String password, String email, String sessionToken, String country, String countryURL, String avatarURL)
    {
        userDAO.addUser(username, password, email, sessionToken, country, countryURL, avatarURL);
    }

    public void updateValue(String column, String value)
    {
        userDAO.updateValue(column, value);
    }

    public void updateValue(String column, Long value)
    {
        userDAO.updateValue(column, value);
    }

    public HashMap<String, Object> getUserDetails()
    {
        return userDAO.getUserDetails();
    }
}
