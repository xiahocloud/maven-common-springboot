package ${package}.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
				.addResourceLocations("classpath:/META-INF/resources/")
				.addResourceLocations("classpath:/resources/**")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/public/**")
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

		/*registry.enableContentNegotiation(new MappingJackson2JsonView());*/
		registry.freeMarker().cache(false);

	}

	/**
	 * freemarker视图解析器
	 */
	@Bean
	public ViewResolver viewResolver() {
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
		viewResolver.setViewClass(FreeMarkerView.class);
		viewResolver.setCache(false);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".ftl");
		viewResolver.setContentType("text/html;charset=UTF-8");
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


	/**
	 * Method Description: Created by whx
	 * 〈 Http消息转换器 〉
	 *
	 * @return org.springframework.http.converter.HttpMessageConverter<java.lang.String>
	 * @throws:
	 * @date 03/01/2019 10:11
	 */
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {

		HttpMessageConverter converter = new StringHttpMessageConverter(
				Charset.forName("UTF-8"));
		return converter;
	}

	public HttpMessageConverter<?> responseBodyFastJsonConverter() {
		//1、定义一个convert转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		supportedMediaTypes.add(MediaType.APPLICATION_PDF);
		supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XML);
		supportedMediaTypes.add(MediaType.IMAGE_GIF);
		supportedMediaTypes.add(MediaType.IMAGE_JPEG);
		supportedMediaTypes.add(MediaType.IMAGE_PNG);
		supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
		supportedMediaTypes.add(MediaType.TEXT_PLAIN);
		supportedMediaTypes.add(MediaType.TEXT_XML);
		fastConverter.setSupportedMediaTypes(supportedMediaTypes);
		fastConverter.setDefaultCharset(Charset.defaultCharset());
		//2、添加fastjson的配置信息
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.PrettyFormat,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.DisableCircularReferenceDetect);
		//3、在convert中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		return fastConverter;

	}

	/**
	 * Method Description: Created by whx
	 * 〈修改HttpMessageConverter默认配置〉
	 *
	 * @param converters 转换器列表
	 * @return void
	 * @throws
	 * @date 03/01/2019 10:15
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 启用fastJson作为消息转换内容
		converters.add(responseBodyFastJsonConverter());

	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		config.addExposedHeader("Content-Type");
		config.addExposedHeader("X-Requested-With");
		config.addExposedHeader("accept");
		config.addExposedHeader("Origin");
		config.addExposedHeader("Access-Control-Request-Method");
		config.addExposedHeader("Access-Control-Request-Headers");
		config.addExposedHeader("Access-Control-Allow-Origin");
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
		configSource.registerCorsConfiguration("/**", config);
		return new CorsFilter(configSource);
	}
}
