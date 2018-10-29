package com.user.manager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(value = "com.user.manager.service.repository")
@Profile({"dev", "default"})
public class DatabaseConfiguration {

  private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfiguration.class);

  private final Environment environment;

  public DatabaseConfiguration(Environment environment) {
    this.environment = environment;
  }

  @Bean(name = "dataSource")
  public DataSource dataSource() {

    LOG.debug("Setting up datasource");

    DriverManagerDataSource datasource = new DriverManagerDataSource();

    datasource.setDriverClassName("com.mysql.jdbc.Driver");
    datasource.setUrl(environment.getProperty("DB_URL"));
    datasource.setUsername(environment.getProperty("DB_USERNAME"));
    datasource.setPassword(environment.getProperty("DB_PASSWORD"));

    return datasource;
  }

  @Bean(name = "entityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      @Qualifier("dataSource") DataSource dataSource,
      @Qualifier("jpaVendorAdapter") JpaVendorAdapter jpaVendorAdapter) {

    LOG.debug("Setting up entity manager factory");

    Properties props = new Properties();
    props.setProperty("eclipselink.weaving", String.valueOf(false));

    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

    emf.setDataSource(dataSource);
    emf.setPackagesToScan("com.user.manager.entity");
    emf.setJpaVendorAdapter(jpaVendorAdapter);
    emf.setJpaProperties(props);

    return emf;
  }

  @Bean(name = "transactionManager")
  @Primary
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {

    LOG.debug("Setting up transaction manager");

    return new JpaTransactionManager(emf);
  }

  @Bean(name = "jpaVendorAdapter")
  @Primary
  public JpaVendorAdapter jpaVendorAdapter() {

    LOG.debug("Setting up jpa vendor adapter");

    EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();

    adapter.setShowSql(false);
    adapter.setGenerateDdl(false);
    adapter.setDatabase(org.springframework.orm.jpa.vendor.Database.MYSQL);
    adapter.setDatabasePlatform("org.eclipse.persistence.platform.database.MySQLPlatform");

    return adapter;
  }
}
