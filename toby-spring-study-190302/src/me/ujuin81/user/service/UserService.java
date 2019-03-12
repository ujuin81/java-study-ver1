package me.ujuin81.user.service;

import me.ujuin81.user.domain.User;

public interface UserService {
	
	void add(User user);
	void upgradeLevels();
}
