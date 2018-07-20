package com.example.demo.da;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatabaseConfiguration {


  @Bean
  @Primary
  @ConfigurationProperties("app.datasource.db1")
  public DataSourceProperties firstDataSourceProperties() {
    return new DataSourceProperties();
  }

  /**
   * DB1 datasource.
   * @return DataSource
   */
  @Bean
  @Primary
  @ConfigurationProperties("app.datasource.db1")
  public DataSource firstDataSource() {
    return firstDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }


  /**
   * DB1 JDBI.
   * @param dataSource DataSource
   * @return Jdbi instance
   */
  @Autowired
  @Bean
  public Jdbi db1Jdbi(DataSource dataSource) {
    synchronized (Jdbi.class) {
      return Jdbi.create(dataSource);
    }
  }

}
