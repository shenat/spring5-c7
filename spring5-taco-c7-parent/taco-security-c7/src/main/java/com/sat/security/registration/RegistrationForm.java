package com.sat.security.registration;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.sat.tacos.User;

import lombok.Data;

//该类不需要持久化，而是通过转化为user持久化
@Data
public class RegistrationForm {
	
	private String username;
	private String password;
	private String fullname;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String phone;
	
	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(
				username,passwordEncoder.encode(password),
				fullname,street,city,state,zip,phone
		);
	}
	
}
