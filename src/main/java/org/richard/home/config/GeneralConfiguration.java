package org.richard.home.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static java.lang.String.format;
import static java.sql.Connection.TRANSACTION_SERIALIZABLE;
import static org.hibernate.cfg.AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS;
import static org.hibernate.cfg.JdbcSettings.ISOLATION;
import static org.hibernate.cfg.PersistenceSettings.*;


@Configuration
public class GeneralConfiguration {
    private static final Logger log = LoggerFactory.getLogger(GeneralConfiguration.class);
    private static String HOST, PORT, USERNAME, PASSWORD, DATABASE_NAME, MONGO_HOST, MONGO_PORT, MONGO_DB_NAME, MONGO_USERNAME, MONGO_PASSWORD, MONGO_BUCKET;

    static {
        Properties props = new Properties();
        try {
            props.load(
                    Files.newInputStream(
                            Path.of(
                                    GeneralConfiguration.class.getClassLoader().getResource("application.properties").toURI())));
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        GeneralConfiguration.HOST = props.getProperty(ApplicationPropertyConstants.HOST);
        GeneralConfiguration.PORT = props.getProperty(ApplicationPropertyConstants.PORT);
        GeneralConfiguration.USERNAME = props.getProperty(ApplicationPropertyConstants.USERNAME);
        GeneralConfiguration.PASSWORD = props.getProperty(ApplicationPropertyConstants.PASSWORD);
        GeneralConfiguration.DATABASE_NAME = props.getProperty(ApplicationPropertyConstants.DATABASE_NAME);

        GeneralConfiguration.MONGO_HOST = props.getProperty(ApplicationPropertyConstants.MONGO_HOST);
        GeneralConfiguration.MONGO_PORT = props.getProperty(ApplicationPropertyConstants.MONGO_PORT);
        GeneralConfiguration.MONGO_DB_NAME = props.getProperty(ApplicationPropertyConstants.MONGO_DB_NAME);
        GeneralConfiguration.MONGO_BUCKET = props.getProperty(ApplicationPropertyConstants.MONGO_BUCKET_NAME);
        GeneralConfiguration.MONGO_USERNAME = props.getProperty(ApplicationPropertyConstants.MONGO_USERNAME);
        GeneralConfiguration.MONGO_PASSWORD = props.getProperty(ApplicationPropertyConstants.MONGO_PASSWORD);
    }

    public GeneralConfiguration() {
    }


    @Bean(name = "hikariDataSource")
    public DataSource hikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("cookbook");
        dataSource.setMaximumPoolSize(200);
        dataSource.setMinimumIdle(2);
        dataSource.addDataSourceProperty("cachePrepStmts", Boolean.TRUE);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 512);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 1024);
        dataSource.addDataSourceProperty("useServerPrepStmts", Boolean.TRUE);
        dataSource.setDriverClassName("org.postgresql.Driver");
        String jdbcUrl = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE_NAME;
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        dataSource.setAutoCommit(false);
        log.info("configuration: url {}, username: {}", jdbcUrl, USERNAME);
        return dataSource;
    }

    @Bean
    public MongoClient mongoClient() {

        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(
                                new ConnectionString(
                                        format("mongodb://%s:%s@%s:%s/%s", MONGO_USERNAME, MONGO_PASSWORD, MONGO_HOST, MONGO_PORT, MONGO_DB_NAME)))
                        .build()
        );
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        Properties jpaProps = new Properties();
        jpaProps.put("hibernate.format_sql", "true");
        jpaProps.put("hibernate.hbm2ddl.auto", "none");
        jpaProps.put("hibernate.show_sql", "true");
        jpaProps.put("hibernate.enable_lazy_load_no_trans", "true");
        jpaProps.put("hibernate.generate_statistics", "true");

        PersistenceUnitInfo persistenceUnitInfo = myPersistenceUnitInfo();
        jpaProps.put(ISOLATION, TRANSACTION_SERIALIZABLE);
        jpaProps.put(CURRENT_SESSION_CONTEXT_CLASS, "thread");
        jpaProps.put(PERSISTENCE_UNIT_NAME, persistenceUnitInfo.getPersistenceUnitName());
        jpaProps.put(JAKARTA_PERSISTENCE_PROVIDER, persistenceUnitInfo.getPersistenceProviderClassName());
        jpaProps.put(JAKARTA_TRANSACTION_TYPE, persistenceUnitInfo.getTransactionType());
        jpaProps.put("jakarta.persistence.nonJtaDataSource", persistenceUnitInfo.getNonJtaDataSource());

        //cache
//        jpaProps.put("hibernate.cache.use_second_level_cache", "true");
//        jpaProps.put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");
//        jpaProps.put("hibernate.cache.region.factory_class", "jcache");
////        jpaProps.put("javax.persistence.sharedCache.mode","all");
//        jpaProps.put("hibernate.cache.use_query_cache", "true");

        return new HibernatePersistenceProvider().createContainerEntityManagerFactory(myPersistenceUnitInfo(), jpaProps);
    }

    private PersistenceUnitInfo myPersistenceUnitInfo() {
        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "rich-persisten-unit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return hikariDataSource();
            }

            @Override
            public List<String> getMappingFileNames() {
                return null;
            }

            @Override
            public List<URL> getJarFileUrls() {
                return null;
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                try {
                    return this.getClass().getClassLoader().getResource("org/richard/home/domain").toURI().toURL();
                } catch (MalformedURLException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<String> getManagedClassNames() {
                return null;
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return null;
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return null;
            }
        };
    }


    @Override
    public String toString() {
        return "MyConfiguration{" +
                "host='" + HOST + '\'' +
                ", port='" + PORT + '\'' +
                ", username='" + USERNAME + '\'' +
                ", password='" + PASSWORD + '\'' +
                '}';
    }

}
