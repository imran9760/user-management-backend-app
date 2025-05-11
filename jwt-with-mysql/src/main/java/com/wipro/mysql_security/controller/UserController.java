package com.wipro.mysql_security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.mysql_security.entity.Users;
import com.wipro.mysql_security.model.JwtTokenModel;
import com.wipro.mysql_security.model.UserRequest;
import com.wipro.mysql_security.service.UserCreationService;

@RestController
@CrossOrigin("http://localhost:5173/")
public class UserController {

	@Autowired
	private UserCreationService service;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void createUser(@RequestBody UserRequest userRequest) throws Exception {
		service.saveUser(userRequest);
	}

	@RequestMapping(value = "/user/update-user", method = RequestMethod.PUT)
	public UserRequest updateUser(@RequestBody UserRequest userRequest) {
		return service.updateUser(userRequest);
	}

	@RequestMapping(value = "/admin/delete-user/{username}", method = RequestMethod.DELETE)
	public String deleteUser(@PathVariable String username) {
		return service.deleteUserByName(username);
	}

	@RequestMapping("/user/users")
	public List<Users> getAllUsers() {
		return service.getAllUsers();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JwtTokenModel login(@RequestBody UserRequest userRequest) {
		return service.verifyUser(userRequest);
	}
}
