package me.ujuin81.user.service;

import java.util.List;

import me.ujuin81.user.domain.User;

public interface UserService {
	
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	void update(User user);
	
	void upgradeLevels();
}
