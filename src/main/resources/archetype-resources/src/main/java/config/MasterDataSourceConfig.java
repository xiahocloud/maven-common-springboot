package ${package}.config;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Andy
 * @version 1.0
 * @date 03/01/2019 10:56
 * @description 首选数据源配置
 */
@Configuration
@EnableTransactionManagement
public class MasterDataSourceConfig {

	@Value("${master.datasource.druid.url}")
	private String url;
	@Value("${master.datasource.druid.driverClassName}")
	private String driverClassName;
	@Value("${master.datasource.druid.username}")
	private String username;
	@Value("${master.datasource.druid.password}")
	private String password;

	/**
	 * Method Description: Created by whx
	 * 〈 配置Druid数据库连接池 〉
	 *
	 * @return com.alibaba.druid.pool.DruidDataSource
	 * @date 03/01/2019 11:01
	 */
	@Bean
	public DruidDataSource dataSource() {
		DruidDataSource ds = new DruidDataSource();
		ds.setUrl(url);
		ds.setDriverClassName(driverClassName);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		String packageName = "${package}";
		packageName.replaceAll(".", "/");
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:" + packageName + "/mapper/*.xml"));
		sessionFactory.setTypeAliasesPackage("${package}");
		return sessionFactory.getObject();
	}
}
