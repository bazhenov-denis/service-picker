package com.example.backend.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class JpaConfig {

//  @Bean(name = "entityManagerFactory")
//  public LocalSessionFactoryBean sessionFactory() {
//    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//    sessionFactory.setDataSource(dataSource());
//    sessionFactory.setPackagesToScan("com.example.backend.models");
//    sessionFactory.setHibernateProperties(hibernateProperties());
//
//    return sessionFactory;
//  }
//
//  @Bean
//  public DataSource dataSource() {
//    BasicDataSource dataSource = new BasicDataSource();
//    dataSource.setDriverClassName("org.postgresql.Driver");
//    dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));
//    dataSource.setUsername(System.getenv("SPRING_DATASOURCE_USERNAME"));
//    dataSource.setPassword(System.getenv("SPRING_DATASOURCE_PASSWORD"));
//
//    return dataSource;
//  }
//
//  @Bean
//  public PlatformTransactionManager hibernateTransactionManager() {
//    HibernateTransactionManager transactionManager
//        = new HibernateTransactionManager();
//    transactionManager.setSessionFactory(sessionFactory().getObject());
//    return transactionManager;
//  }
//
//  private final Properties hibernateProperties() {
//    Properties hibernateProperties = new Properties();
//    hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
//    hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//    hibernateProperties.setProperty("hibernate.show_sql", "true");
//    hibernateProperties.setProperty("hibernate.format_sql", "true");
//
//    return hibernateProperties;
//  }

  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setDataSource(this.getDataSource());
    entityManagerFactory.setPackagesToScan("com.example.backend.models");
    //factoryBean.setPersistenceUnitName("MyMy");
    entityManagerFactory.setJpaVendorAdapter(getVendorAdapter());

    final Map<String, String> jpaProperties = new HashMap<String, String>();
    jpaProperties.put("hibernate.format_sql", "true");
    //jpaProperties.put("hibernate.connection.charset", "UTF-8");
    jpaProperties.put("hibernate.hbm2ddl.auto", "none");
    entityManagerFactory.setJpaPropertyMap(jpaProperties);
    return entityManagerFactory;
  }

  @Bean
  public JpaVendorAdapter getVendorAdapter() {
    final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
    jpaVendorAdapter.setShowSql(true);
    return jpaVendorAdapter;
  }

  @Bean
  public DataSource getDataSource(){
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));
    dataSource.setUsername(System.getenv("SPRING_DATASOURCE_USERNAME"));
    dataSource.setPassword(System.getenv("SPRING_DATASOURCE_PASSWORD"));
    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(this.entityManagerFactoryBean().getObject());
    return transactionManager;
  }
}
