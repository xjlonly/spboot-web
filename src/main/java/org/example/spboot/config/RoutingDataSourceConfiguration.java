package org.example.spboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class RoutingDataSourceConfiguration{

    @Primary
    @Bean
    DataSource dataSource(@Autowired @Qualifier(RoutingDataSourceContext.MASTER_DATASOURCE) DataSource masterDataSource,
                            @Autowired @Qualifier(RoutingDataSourceContext.SLAVE_DATASOURCE) DataSource slaveDataSource){
            var routingDataSource = new RoutingDataSource();
            routingDataSource.setTargetDataSources(
                    Map.of(RoutingDataSourceContext.MASTER_DATASOURCE,masterDataSource,
                            RoutingDataSourceContext.SLAVE_DATASOURCE,slaveDataSource));
            routingDataSource.setDefaultTargetDataSource(masterDataSource);
            return routingDataSource;
    }

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    DataSourceTransactionManager createTransactionManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}

class RoutingDataSource extends AbstractRoutingDataSource{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected DataSource determineTargetDataSource() {
        var ds = super.determineTargetDataSource();
        logger.info("determine target datasource:{}", ds);
        return ds;
    }


    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getRoutingDataSourceKey();
    }

}

