package me.ujuin81.user.sqlservice;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}