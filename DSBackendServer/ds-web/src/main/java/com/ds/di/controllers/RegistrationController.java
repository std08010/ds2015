/**
 * 
 */
package com.ds.di.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ds.di.model.user.User;
import com.ds.di.service.user.UserService;
import com.ds.di.utils.RegistrationForm;

/**
 * @author Altin Cipi
 *
 */
@Controller
public class RegistrationController
{
	@Autowired
	@Qualifier(UserService.SPRING_KEY)
	private UserService	userService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String showMainView()
	{
		return "index";
	}

	@ModelAttribute("index")
	public RegistrationForm getRegistrationForm()
	{
		return new RegistrationForm();
	}

	@Transactional
	@RequestMapping(value = "index", method = RequestMethod.POST)
	public String processRegisterUser(@ModelAttribute("index") RegistrationForm form)
	{

		User user = new User();
		user.setEmail(form.getEmail());
		user.setUsername(form.getUsername());
		user.setPassword(form.getPassword());

		userService.create(user);

		return "index";
	}

	@Transactional
	@RequestMapping(value = "index/show-users", method = RequestMethod.GET)
	public String showAllUsers(Map<String, Object> model)
	{
		List<User> accounts = userService.findAll();
		model.put("accounts", accounts);
		return "index";
	}
}
