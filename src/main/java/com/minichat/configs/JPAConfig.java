package com.minichat.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"com.minichat"})
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.minichat.repositories")
@PropertySource("classpath:/application.properties")
public class JPAConfig {

    private Environment env;

    @Autowired
    public JPAConfig(Environment env) {
        this.env = env;
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {

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
        return dataSource;
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
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}
