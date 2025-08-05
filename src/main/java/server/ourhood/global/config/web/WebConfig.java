package server.ourhood.global.config.web;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import server.ourhood.global.auth.resolver.LoginUserArgumentResolver;
import server.ourhood.global.config.converter.StringToOAuthTypeConverter;
import server.ourhood.global.config.converter.StringToRoomSearchConditionConverter;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoginUserArgumentResolver loginUserArgumentResolver;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToRoomSearchConditionConverter());
		registry.addConverter(new StringToOAuthTypeConverter());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserArgumentResolver);
	}
}
