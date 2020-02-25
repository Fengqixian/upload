package com.clinbrain.bd.mdm.common.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.clinbrain.bd.mdm.common.datasource.support.DataSourceConstants;
import com.clinbrain.bd.mdm.common.datasource.util.DataDBType;
import com.clinbrain.bd.mdm.common.datasource.util.DataDbUrl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.PostConstruct;
import java.sql.Driver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lianglele
 * @date 2019-12-26 9:36
 * 动态数据源切换配置
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class DynamicDataSourceConfig implements TransactionManagementConfigurer {
    private final Map<Object, Object> dataSourceMap = new HashMap<>(8);
    private final DruidDataSourceProperties dataSourceProperties;
    private final DataDbUrl dataDbUrl;

    @Bean("dynamicDataSource")
    public DynamicDataSource dataSource() {
        DynamicDataSource ds = new DynamicDataSource();
        DruidDataSource cads = new DruidDataSource();
        cads.setUrl(dataSourceProperties.getUrl());
        cads.setDriverClassName(dataSourceProperties.getDriverClassName());
        cads.setUsername(dataSourceProperties.getUsername());
        cads.setPassword(dataSourceProperties.getPassword());
        ds.setDefaultTargetDataSource(cads);
        dataSourceMap.put(0, cads);
        ds.setTargetDataSources(dataSourceMap);
        return ds;
    }

    /**
     * 组装默认配置的数据源，查询数据库配置
     */
    @PostConstruct
    public void init() {
        DriverManagerDataSource dds = new DriverManagerDataSource();
        dds.setUrl(dataSourceProperties.getUrl());
        dds.setDriverClassName(dataSourceProperties.getDriverClassName());
        dds.setUsername(dataSourceProperties.getUsername());
        dds.setPassword(dataSourceProperties.getPassword());

        List<Map<String, Object>> dbList = new JdbcTemplate(dds).queryForList(DataSourceConstants.QUERY_DS_SQL);
        log.info("开始 -> 初始化动态数据源");
        Optional.of(dbList).ifPresent(list -> list.forEach(db -> {
            log.info("数据源:{}", db.get(DataSourceConstants.DS_NAME));
            DruidDataSource ds = new DruidDataSource();
            DataDBType dbType = DataDBType.parseValue(db.get(DataSourceConstants.DS_DB_TYPE) + "");
            try {
                String url = dataDbUrl.getDbUrl(db.get(DataSourceConstants.DS_JDBC_IP) + "", db.get(DataSourceConstants.DS_JDBC_PROT) + "", db.get(DataSourceConstants.DS_NAME) + "", dbType);
                ds.setUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("初始化动态数据源失败:{}", db.get(DataSourceConstants.DS_NAME));
            }


//            db.get(DataSourceConstants.DS_JDBC_IP) + ":" + db.get(DataSourceConstants.DS_JDBC_PROT)));
            ds.setDriverClassName(dbType.getDriverClass());
            ds.setUsername((String) db.get(DataSourceConstants.DS_USER_NAME));

//            String decPwd = stringEncryptor.decrypt((String) db.get(DataSourceConstants.DS_USER_PWD));
            ds.setPassword(db.get(DataSourceConstants.DS_USER_PWD) + "");
            ds.setConnectionErrorRetryAttempts(0);
            ds.setBreakAfterAcquireFailure(true);
//            ds.setAsyncInit(false);
//            ds.setRemoveAbandonedTimeoutMillis(1);
//            ds.setRemoveAbandonedTimeout(1);
//            ds.setRemoveAbandoned(true);
            ds.setMaxWait(1000);
            dataSourceMap.put(db.get(DataSourceConstants.DS_ROUTE_KEY), ds);
        }));

        log.info("完毕 -> 初始化动态数据源,共计 {} 条", dataSourceMap.size());
    }

    /**
     * 重新加载数据源配置
     */
    public Boolean reload() {
        init();
        DynamicDataSource dataSource = dataSource();
        dataSource.setTargetDataSources(dataSourceMap);
        dataSource.afterPropertiesSet();
        return Boolean.FALSE;
    }


    @Bean
    public PlatformTransactionManager txManager() {
        DataSourceTransactionManager transactionManager
                = new DataSourceTransactionManager(dataSource());
        return new ChainedTransactionManager(transactionManager);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }
}
