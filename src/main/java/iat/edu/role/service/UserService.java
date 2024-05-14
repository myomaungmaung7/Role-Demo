package iat.edu.role.service;

import java.util.List;

import iat.edu.role.model.User;

public interface UserService {
	
	User authenticate(String userName, String password);
	List <User> findAllUsers();
	User createUser (User user);
}
