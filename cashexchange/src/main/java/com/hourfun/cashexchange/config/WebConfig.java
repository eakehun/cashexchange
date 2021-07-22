package com.hourfun.cashexchange.config;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hourfun.cashexchange.filter.RequestBodyLoggingFilter;
import com.hourfun.cashexchange.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("authorization", "content-type").allowCredentials(true).maxAge(3600);
	}

	@Bean
	public ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();

		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return mapper;
	}

	@Bean
	public MappingJackson2HttpMessageConverter converter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

		converter.setObjectMapper(getMapper());
		return converter;
	}

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		return new StringHttpMessageConverter(StandardCharsets.UTF_8);
	}

	@Bean
	public HandlerInterceptorAdapter authInterCeptor() {
		return new AuthInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterCeptor())
				.addPathPatterns("/users/**", "/admin/**", "/manager/**", "/board/**", "/macro/**")
				.excludePathPatterns("/users/login/", "/admin/login/", "/manager/login/");
	}

	@Bean
	public FilterRegistrationBean<RequestBodyLoggingFilter> getFilterRegistrationBean() {
		return new FilterRegistrationBean<RequestBodyLoggingFilter>(new RequestBodyLoggingFilter());
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();

		return commonsMultipartResolver;
	}

	@Bean
	public RedisSerializer<Object> valueSerializer() {
		return new GenericJackson2JsonRedisSerializer(getMapper());
	}

}
