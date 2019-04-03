package ${package}.config;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

		servletRegistrationBean.addInitParameter("enabled", "true");
		//白名单：
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		//IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的即提示:Sorry, you are not permitted to view this page.
		servletRegistrationBean.addInitParameter("deny", "192.168.30.37");
		//登录查看信息的账号密码.
		servletRegistrationBean.addInitParameter("loginUsername", "druid");
		servletRegistrationBean.addInitParameter("loginPassword", "druid");
		//是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		//添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");
		//添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filterRegistrationBean.addInitParameter("enabled", "true");
		filterRegistrationBean.addInitParameter("profile-enable", "true");
		return filterRegistrationBean;
	}


	/**
	 * Method Description: Created by whx
	 * 〈 配置Druid数据库连接池 〉
	 *
	 * @return com.alibaba.druid.pool.DruidDataSource
	 * @date 03/01/2019 11:01
	 */
	@Primary
	@Bean(name = "masterDatasource")
	@ConfigurationProperties("master.datasource.druid")
	public DruidDataSource dataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "masterSqlSessionFactory")
	@ConditionalOnBean(name = "masterDatasource")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		String packageName = "${package}";
		packageName = packageName.replace(".", "/");
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:" + packageName + "/mapper/*.xml"));
		sessionFactory.setTypeAliasesPackage("${package}.pojo");
		sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		return sessionFactory.getObject();
	}

	@Primary
	@Bean(name = "masterSqlSessionTemplate")
	@ConditionalOnBean(name = "masterSqlSessionFactory")
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Primary
	@Bean(name = "masterDataSourceTransactionManager")
	@ConditionalOnBean(name = "masterDatasource")
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
