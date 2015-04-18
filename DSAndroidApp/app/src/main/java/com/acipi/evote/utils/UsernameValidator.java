package com.acipi.evote.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Altin Cipi on 2/6/2015.
 */
public class UsernameValidator
{
    private Pattern pattern;
    private Matcher matcher;

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_.]{4,30}$";

    public UsernameValidator()
    {
        pattern = Pattern.compile(USERNAME_PATTERN);
    }

    /**
     * Validate username with regular expression
     *
     * @param username username for validation
     * @return true valid username, false invalid username
     */
    public boolean validate(final String username)
    {
        matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
