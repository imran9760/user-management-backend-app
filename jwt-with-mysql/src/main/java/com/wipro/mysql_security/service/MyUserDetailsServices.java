package com.wipro.mysql_security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wipro.mysql_security.entity.Users;
import com.wipro.mysql_security.model.MyUserDetails;
import com.wipro.mysql_security.repository.UserRepository;

@Service
public class MyUserDetailsServices implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users = userRepository.findByUsername(username);
		System.out.println(users.getUsername());
		System.out.println(users.getPassword());
		users.getRoles().stream().map(entry -> entry.getName()).forEach(System.out::println);
		return new MyUserDetails(users);
	}

}
