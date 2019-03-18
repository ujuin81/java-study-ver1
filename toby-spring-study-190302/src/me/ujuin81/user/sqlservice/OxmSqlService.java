package me.ujuin81.user.sqlservice;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import me.ujuin81.user.sqlservice.jaxb.SqlType;
import me.ujuin81.user.sqlservice.jaxb.Sqlmap;

public class OxmSqlService implements SqlService {
	//loadSql, getSql이 BaseSqlService와 중복 -> BaseSqlService로 위임 
	private final BaseSqlService baseSqlService = new BaseSqlService();
	private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
	
	private SqlRegistry sqlRegistry = new HashMapSqlRegistry();
	
	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.oxmSqlReader.setUnmarshaller(unmarshaller);
	}
	
	public void setSqlmap(Resource sqlmap) {
		this.oxmSqlReader.setSqlmap(sqlmap);
	}
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	
	@PostConstruct
	public void loadSql() {		
		this.baseSqlService.setSqlReader(this.oxmSqlReader);
		this.baseSqlService.setSqlRegistry(this.sqlRegistry);
		
		this.baseSqlService.loadSql(); 
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		return this.baseSqlService.getSql(key); 
	}
	
	private class OxmSqlReader implements SqlReader{
		private Unmarshaller unmarshaller;
		private Resource sqlmap = new ClassPathResource("/sqlmap.xml");
		
		public void setUnmarshaller(Unmarshaller unmarshaller) {
			this.unmarshaller = unmarshaller;
		}
		
		public void setSqlmap(Resource sqlmap) {
			this.sqlmap = sqlmap;
		}

		@Override
		public void read(SqlRegistry sqlRegistry) {
			try {
				Source source = new StreamSource(sqlmap.getInputStream());
				Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(source);
				
				for(SqlType sql : sqlmap.getSql()) {
					sqlRegistry.registerSql(sql.getKey(), sql.getValue());
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(this.sqlmap.getFilename() + "을 가져올 수 없습니다.", e);
			}
		}
		
	}

}
