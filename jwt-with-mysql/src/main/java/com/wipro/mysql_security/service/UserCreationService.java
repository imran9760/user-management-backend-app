package com.wipro.mysql_security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.mysql_security.entity.Roles;
import com.wipro.mysql_security.entity.Users;
import com.wipro.mysql_security.model.JwtTokenModel;
import com.wipro.mysql_security.model.UserRequest;
import com.wipro.mysql_security.repository.RolesRepository;
import com.wipro.mysql_security.repository.UserRepository;

@Service
public class UserCreationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RolesRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	public void saveUser(UserRequest userRequest) throws Exception {
		// create new user
		if (userRepository.findByUsername(userRequest.getUsername()) != null) {
			throw new Exception("Username already exists");
		}
		Users user = new Users();
		String encodedPassword = encoder.encode(userRequest.getPassword());
		user.setUsername(userRequest.getUsername());
		user.setPassword(encodedPassword);
		user.setEnabled(userRequest.isEnabled());
		userRepository.save(user);

		// assign role for user i.e save entry in user_roles table
		Roles role = roleRepository.findByName("ROLE_" + userRequest.getRole().toUpperCase());
		if (role == null) {
			throw new Exception("Role not found");
		}
		user = userRepository.findByUsername(userRequest.getUsername());
		user.getRoles().add(role);
		userRepository.save(user);
	}

	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}

	// Manual validate user authentication to pass username and password
	public JwtTokenModel verifyUser(UserRequest userRequest) {
		JwtTokenModel token = new JwtTokenModel();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			token.setToken(jwtService.generateToken(userRequest.getUsername()));
			return token;
		}
		return null;
	}

	public UserRequest updateUser(UserRequest userRequest) {
		Users users = userRepository.findByUsername(userRequest.getUsername());
		if (users != null) {
			users.setPassword(encoder.encode(userRequest.getPassword()));
			userRepository.save(users);
			return userRequest;
		}
		return null;
	}

	public String deleteUserByName(String username) {
		Users users = userRepository.findByUsername(username);
		if (users != null) {
			userRepository.deleteById(users.getId());
			return "Successfully Deleted";
		}
		return "Data Not found";
	}
}
