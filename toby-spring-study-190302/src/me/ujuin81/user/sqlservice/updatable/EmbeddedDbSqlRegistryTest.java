package me.ujuin81.user.sqlservice.updatable;

import static org.junit.Assert.fail;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import me.ujuin81.user.sqlservice.SqlUpdateFailureException;
import me.ujuin81.user.sqlservice.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	EmbeddedDatabase db;

	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		db = new EmbeddedDatabaseBuilder().setType(HSQL)
				.addScript("classpath:/me/ujuin81/user/sqlservice/updatable/sqlRegistrySchema.sql").build();

		EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
		embeddedDbSqlRegistry.setDataSource(db);

		return embeddedDbSqlRegistry;
	}

	@After
	public void tearDown() {
		db.shutdown();
	}

	@Test
	public void transactionUpdate() {
		checkFindResult("SQL1", "SQL2", "SQL3");

		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY9999!@#$", "Modified9999");

		try {
			sqlRegistry.updateSql(sqlmap);
			fail();
		} catch (SqlUpdateFailureException e) {
		}

		checkFindResult("SQL1", "SQL2", "SQL3");
	}

}
