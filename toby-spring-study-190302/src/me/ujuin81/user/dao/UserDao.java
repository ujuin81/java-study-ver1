package me.ujuin81.user.dao;

import java.util.List;

import me.ujuin81.user.domain.User;

public interface UserDao {	
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
	void update(User user);
}