package com.example.blps_lab1.config.db_config;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "ExtendedDataSourceConfiguration",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.example.blps_lab1.repository.extended"}
)
public class ExtendedDataSourceConfiguration {
    public Map<String, String> extendedJpaProperties() {
        Map<String, String> extendedJpaProperties = new HashMap<>();
        extendedJpaProperties.put("hibernate.hbm2ddl.auto", "update");
        extendedJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        extendedJpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        extendedJpaProperties.put("javax.persistence.transactionType", "JTA");
        return extendedJpaProperties;
    }

    @Bean(name = "extendedEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder extendedEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), extendedJpaProperties(), null);
    }

    @Bean(name = "ExtendedDataSourceConfiguration")
    public LocalContainerEntityManagerFactoryBean extendedEntityManager(
            @Qualifier("extendedEntityManagerFactoryBuilder") EntityManagerFactoryBuilder extendedEntityManagerFactoryBuilder,
            @Qualifier("extendedDataSource") DataSource postgresDataSource
    ) {
        return extendedEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps_lab1.model.extended")
                .persistenceUnit("postgres")
                .properties(extendedJpaProperties())
                .jta(true)
                .build();
    }

    @Bean("extendedDataSourceProperties")
    @ConfigurationProperties("spring.datasource.extended")
    public DataSourceProperties extendedDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean("extendedDataSource")
    public DataSource extendedDataSource(@Qualifier("extendedDataSourceProperties") DataSourceProperties extendedDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(extendedDataSourceProperties.getUrl());
        ds.setUser(extendedDataSourceProperties.getUsername());
        ds.setPassword(extendedDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setUniqueResourceName("xa_extended");
        return xaDataSource;
    }

}
