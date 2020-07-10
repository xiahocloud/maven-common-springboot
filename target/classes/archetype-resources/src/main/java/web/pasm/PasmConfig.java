package ${package}.web.pasm;

import ${package}.web.filter.PasmFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description:
 *
 * @author Andy
 * @version 1.0
 * @date 05/30/2019 16:53
 */
@Configuration
public class PasmConfig {
/*	@Bean
	public FilterRegistrationBean pasmFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new PasmFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns(new String[]{"/*"});
		filterRegistration.setOrder(2);
		return filterRegistration;
	}*/
}
