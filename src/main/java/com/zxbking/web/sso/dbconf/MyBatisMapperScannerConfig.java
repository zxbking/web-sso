
package com.zxbking.web.sso.dbconf;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * MyBatis扫描接口，使用的tk.mybatis.spring.mapper.MapperScannerConfigurer，如果你不使用通用Mapper，可以改为org.xxx...
 *
 * @author jame
 * @since 2016-9-19 14:46
 */
@Configuration
//TODO 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
@AutoConfigureAfter({MyBatisRdsConfig.class})
public class MyBatisMapperScannerConfig {

    @Bean(name = "rdsMapperScannerConfigurer")
    public MapperScannerConfigurer testMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("rdsSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(MyBatisRdsConfig.PACKAGE);
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.isuwang.web.sso.dbconf.MyMapper");
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
