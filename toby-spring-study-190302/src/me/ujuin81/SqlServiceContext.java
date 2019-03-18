package me.ujuin81;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import me.ujuin81.user.dao.UserDao;
import me.ujuin81.user.sqlservice.OxmSqlService;
import me.ujuin81.user.sqlservice.SqlRegistry;
import me.ujuin81.user.sqlservice.SqlService;
import me.ujuin81.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

@Configuration
public class SqlServiceContext {
	@Autowired SqlMapConfig sqlMapConfig;

	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		sqlService.setSqlmap(this.sqlMapConfig.getSqlMapResource());
		return sqlService;
	}
	
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}

	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("me.ujuin81.user.sqlservice.jaxb");
		return marshaller;
	}

	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
				.setType(HSQL)
				.addScript("classpath:/me/ujuin81/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
	}

}
