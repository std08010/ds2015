package com.ds.di.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ds.di.model.BaseEntity;
import com.ds.di.model.general.Country;

/**
 * @author Altin Cipi
 *
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity
{
	private static final long	serialVersionUID	= -509055246678550326L;

	@Column(name = "username", nullable = false, unique = true, length = 40)
	private String				username;

	@Column(name = "password", nullable = false, length = 1000)
	private String				password;

	@Column(name = "email", nullable = false, length = 100)
	private String				email;

	@Column(name = "session_token", length = 1000)
	private String				sessionToken;

	@ManyToOne
	@JoinColumn(name = "country_fk")
	private Country				country;

	@Column(name = "avatar_url")
	private String				avatarURL;

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

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email the email to set
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
	 * @param sessionToken the sessionToken to set
	 */
	public void setSessionToken(String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	/**
	 * @return the country
	 */
	public Country getCountry()
	{
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country)
	{
		this.country = country;
	}

	/**
	 * @return the avatarURL
	 */
	public String getAvatarURL()
	{
		return avatarURL;
	}

	/**
	 * @param avatarURL the avatarURL to set
	 */
	public void setAvatarURL(String avatarURL)
	{
		this.avatarURL = avatarURL;
	}
}