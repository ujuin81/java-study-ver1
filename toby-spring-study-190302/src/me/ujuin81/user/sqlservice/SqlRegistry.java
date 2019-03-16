package me.ujuin81.user.sqlservice;

public interface SqlRegistry {
	void registerSql(String key, String Value);
	
	String findSql(String key) throws SqlNotFoundException;
}
