package ${package}.config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Andy
 * @version 1.0
 * @date 03/01/2019 10:56
 * @description 首选数据源配置
 */
public class MasterDataSourceConditionConfig implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata arg1) {
		if (!context.getEnvironment().containsProperty("spring.datasource.master.url")) {
			return false;
		}
		if (!context.getEnvironment().containsProperty("spring.datasource.master.driverClassName")) {
			return false;
		}
		if (!context.getEnvironment().containsProperty("spring.datasource.master.username")) {
			return false;
		}
		if (!context.getEnvironment().containsProperty("spring.datasource.master.password")) {
			return false;
		}
		String url = context.getEnvironment().getProperty("spring.datasource.master.url");
		String driverClassName = context.getEnvironment().getProperty("spring.datasource.master.driverClassName");
		String username = context.getEnvironment().getProperty("spring.datasource.master.username");
		String password = context.getEnvironment().getProperty("spring.datasource.master.password");
		if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(driverClassName)
				&& StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
			return true;
		}
		return false;
	}
}
