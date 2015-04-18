/**
 *
 */
package com.acipi.evote.rest.dto.user;

import com.acipi.evote.rest.dto.AbstractInDTO;

/**
 * @author Altin Cipi
 */
public class LoginAskInDTO extends AbstractInDTO
{
    private String username;
    private String password;

    /**
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
}
