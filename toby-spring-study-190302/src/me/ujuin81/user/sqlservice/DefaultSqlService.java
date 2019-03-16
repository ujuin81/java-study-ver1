package me.ujuin81.user.sqlservice;

public class DefaultSqlService extends BaseSqlService {

	public DefaultSqlService() {
		setSqlReader(new JaxbSqlReader());
		setSqlRegistry(new HashMapSqlRegistry());
	}
}
