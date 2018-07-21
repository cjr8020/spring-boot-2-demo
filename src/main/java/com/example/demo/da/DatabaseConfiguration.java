package com.example.demo.da;

import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DatabaseConfiguration {


  /**
   * DB1 datasource.
   *
   * @return DataSource
   */
  @Bean
  @Primary
  public DataSource dataSource() {
    return (new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .setName("demodb;INIT=CREATE SCHEMA IF NOT EXISTS DEMO;MODE=Oracle;MV_STORE=FALSE;MVCC=FALSE;")
        .addScript("classpath:db/dropcreate.sql")
        .addScript("classpath:db/initdata.sql")
    ).build();
  }


  /**
   * JDBI.
   *
   * @param dataSource DataSource
   * @return Jdbi instance
   */
  @Autowired
  @Bean
  public Jdbi jdbi(DataSource dataSource) {
    synchronized (Jdbi.class) {
      Jdbi jdbi = Jdbi.create(dataSource);
      jdbi.installPlugin(new SqlObjectPlugin());
      return jdbi;
    }
  }

//  @Bean
//  @Primary
//  @ConfigurationProperties("app.datasource.db1")
//  public DataSourceProperties firstDataSourceProperties() {
//    return new DataSourceProperties();
//  }
//
//  /**
//   * DB1 datasource.
//   * @return DataSource
//   */
//  @Bean
//  @Primary
//  @ConfigurationProperties("app.datasource.db1")
//  public DataSource firstDataSource() {
//    return firstDataSourceProperties()
//        .initializeDataSourceBuilder()
//        .type(HikariDataSource.class)
//        .build();
//  }
//
//
//  /**
//   * DB1 JDBI.
//   * @param dataSource DataSource
//   * @return Jdbi instance
//   */
//  @Autowired
//  @Bean
//  public Jdbi db1Jdbi(DataSource dataSource) {
//    synchronized (Jdbi.class) {
//      Jdbi db1Jdbi = Jdbi.create(dataSource);
//      db1Jdbi.installPlugin(new SqlObjectPlugin());
//      return db1Jdbi;
//    }
//  }

}
