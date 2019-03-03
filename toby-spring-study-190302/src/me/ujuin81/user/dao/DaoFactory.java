package me.ujuin81.user.dao;

public class DaoFactory {

	public UserDao userDao() {
		return new UserDao(connectionMaker());				
	}
	
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
