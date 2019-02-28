package ${package}.config;

import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;

/**
 * @author Andy
 * @version 1.0
 * @date 02/28/2019 11:39
 * @description Web 配置
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Method Description: Created by whx
	 * 〈默认请求路径配置〉
	 *
	 * @param registry
	 * @return void
	 * @throws
	 * @date 02/28/2019 11:43
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 前者是匹配路径， 后者是文件名
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/index").setViewName("index");
	}

	/**
	 * Method Description: Created by whx
	 * 〈
	 * 静态文件位置指定路径（当前配置指定了/public, /static）
	 * By default, Spring Boot serves static content from a directory called
	 * /static (or /public or /resources or /META-INF/resources)
	 * in the classpath or from the root of the ServletContext
	 * 〉
	 *
	 * @param registry
	 * @return void
	 * @throws:
	 * @date 02/28/2019 14:24
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("/public/", "classpath:/static/")
				.setCachePeriod(31556926);
	}


	/**
	 * Method Description: Created by whx
	 * 〈 视图解析器在这里注册 〉
	 *
	 * @param registry 注册机
	 * @return void
	 * @throws:
	 * @date 02/28/2019 15:08
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {

		registry.enableContentNegotiation(new MappingJackson2JsonView());
		registry.freeMarker().cache(false);

	}

	/**
	 * freemarker视图解析器
	 */
	@Bean
	public ViewResolver viewResolver(){
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
		viewResolver.setViewClass(FreeMarkerView.class);
		viewResolver.setCache(false);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".ftl");
		viewResolver.setContentType("text/html;charset=utf-8");
		viewResolver.setExposeRequestAttributes(true);
		viewResolver.setExposeSessionAttributes(true);
		viewResolver.setExposeSpringMacroHelpers(true);
		viewResolver.setRequestContextAttribute("request");
		return viewResolver;
	}

	/**
	 * Method Description: Created by whx
	 * 〈freemarker 视图解析器配置〉
	 *
	 * @return org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
	 * @throws IOException TemplateException
	 * @date 02/28/2019 14:44
	 */
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {

		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("classpath:/template/freemarker");
		configurer.setDefaultEncoding("UTF-8");
		return configurer;
	}
}
