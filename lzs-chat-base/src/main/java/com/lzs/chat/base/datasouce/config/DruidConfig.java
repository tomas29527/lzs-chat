package com.lzs.chat.base.datasouce.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * druid 配置多数据源
 *
 * @author ims
 */
@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource masterDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }



//    @Bean     //声明其为Bean实例
//    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        DruidDataSource datasource = new DruidDataSource();
//        List<Filter> filters = new ArrayList<>();
//        filters.add(wallFilter);
//        datasource.setProxyFilters(filters);
//
//        return datasource;
//    }
//
//    @Autowired
//    WallFilter wallFilter;

//    @Bean(name = "wallConfig")
//    WallConfig wallFilterConfig() {
//        WallConfig wc = new WallConfig();
//        wc.setMultiStatementAllow(true);
//        return wc;
//    }
//
//    @Bean(name = "wallFilter")
//    @DependsOn("wallConfig")
//    WallFilter wallFilter(WallConfig wallConfig) {
//        WallFilter wfilter = new WallFilter();
//        wfilter.setConfig(wallConfig);
//        return wfilter;
//    }
}
