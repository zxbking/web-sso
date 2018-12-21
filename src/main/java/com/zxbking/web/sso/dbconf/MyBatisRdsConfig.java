package com.zxbking.web.sso.dbconf;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by zhangxibin on 2017/8/26.
 */
@Configuration
@EnableTransactionManagement
public class MyBatisRdsConfig {
    //master dao 所在的包
    public static final String PACKAGE = "com.isuwang.web.sso.dbmapper";

    //mapper所在目录
    private static final String MAPPER_LOCATION = "classpath:mapper/*.xml";
    @Value("${spring.datasource.driverClassName}") String driver;
    @Value("${spring.datasource.url}") String url;
    @Value("${spring.datasource.username}") String username;
    @Value("${spring.datasource.password}") String password;
    @Value("${spring.datasource.initial-size:5}") String initialSize;
    @Bean(name="rdsDataSource")
    @Primary
    public DataSource druidDataSource(
    ) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(Integer.valueOf(initialSize));
        try {
            druidDataSource.setFilters("stat, wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    @Bean(name="rdsSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(druidDataSource());
        //bean.setTypeAliasesPackage("com.pg.message.entity");

        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        //添加插件
        bean.setPlugins(new Interceptor[]{pageHelper});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resource = new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION);
            bean.setMapperLocations(resource);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "rdsSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("rdsSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name="rdsDataSourceTransactionManager")
    @Primary
    public PlatformTransactionManager annotationDrivenTransactionManager(
            @Qualifier("rdsDataSource")DataSource dataSource
    ) {
        return new DataSourceTransactionManager(dataSource);
    }
}