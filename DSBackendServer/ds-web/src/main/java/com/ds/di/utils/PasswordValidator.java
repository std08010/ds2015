package com.ds.di.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Altin Cipi on 2/6/2015.
 */
public class PasswordValidator
{
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9@#$%]{6,20}$";

    public PasswordValidator()
    {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate username with regular expression
     *
     * @param password username for validation
     * @return true valid username, false invalid username
     */
    public boolean validate(final String password)
    {
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
