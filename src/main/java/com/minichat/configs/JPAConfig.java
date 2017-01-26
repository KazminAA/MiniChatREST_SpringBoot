package com.minichat.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/application.properties")
public class JPAConfig implements TransactionManagementConfigurer {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "close")
    public DataSource configureDataSource() {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(env.getRequiredProperty("db.driver"));
        hikariConfig.setJdbcUrl(env.getRequiredProperty("db.url"));
        hikariConfig.setUsername(env.getRequiredProperty("db.user"));
        hikariConfig.setPassword(env.getRequiredProperty("db.pwd"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty("max.pool.size")));

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.useUnicode", "true");
        hikariConfig.addDataSourceProperty("dataSource.characterEncoding", "utf-8");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return (DataSource) dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        localContainerEntityManagerFactory.setDataSource(dataSource);
        localContainerEntityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactory.setPackagesToScan("com.minichat.models");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.format.sql", env.getRequiredProperty("hibernate.format.sql"));
        jpaProperties.put("hibernate.show.sql", env.getRequiredProperty("hibernate.show.sql"));

        localContainerEntityManagerFactory.setJpaProperties(jpaProperties);

        return localContainerEntityManagerFactory;
    }


    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new JpaTransactionManager();
    }
}
