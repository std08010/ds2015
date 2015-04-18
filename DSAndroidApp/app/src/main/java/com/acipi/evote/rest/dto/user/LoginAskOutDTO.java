/**
 *
 */
package com.acipi.evote.rest.dto.user;

import com.acipi.evote.rest.dto.AbstractOutDTO;

/**
 * @author Altin Cipi
 */
public class LoginAskOutDTO extends AbstractOutDTO
{
    private String	email;
    private String	sessionToken;
    private String	country;
    private String	countryURL;
    private String	avatarURL;

    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @return the sessionToken
     */
    public String getSessionToken()
    {
        return sessionToken;
    }

    /**
     * @param sessionToken
     *            the sessionToken to set
     */
    public void setSessionToken(String sessionToken)
    {
        this.sessionToken = sessionToken;
    }

    /**
     * @return the country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * @return the countryURL
     */
    public String getCountryURL()
    {
        return countryURL;
    }

    /**
     * @param countryURL
     *            the countryURL to set
     */
    public void setCountryURL(String countryURL)
    {
        this.countryURL = countryURL;
    }

    /**
     * @return the avatarURL
     */
    public String getAvatarURL()
    {
        return avatarURL;
    }

    /**
     * @param avatarURL
     *            the avatarURL to set
     */
    public void setAvatarURL(String avatarURL)
    {
        this.avatarURL = avatarURL;
    }
}
