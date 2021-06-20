package com.apigate.customer_info_service.repository.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@Configuration
@Component
@PropertySource("classpath:liquibase.properties")
public class LiquibaseConfig {

	public static final String MASTER_FILE = "classpath:db-schema/master.xml";

	@Value("${spring.liquibase.default-schema}")
	private String defaultSchema;

	@Value("${spring.liquibase.liquibase-schema}")
	private String liquibaseSchema;

	@Value("${spring.liquibase.enabled}")
	private boolean liquibaseEnabled;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.liquibase.rollback-file}")
	private String rollbackFile;

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(MASTER_FILE);
		liquibase.setDataSource(dataSource);
		liquibase.setDefaultSchema(defaultSchema);
		liquibase.setShouldRun(liquibaseEnabled);

		if(rollbackFile!=null && !rollbackFile.isEmpty()) {
			liquibase.setRollbackFile(new File(rollbackFile));
		}

		liquibase.setTestRollbackOnUpdate(true);
		return liquibase;
	}
}
