package server.ourhood.global.auth.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import server.ourhood.domain.auth.controller.HeaderHandler;
import server.ourhood.domain.auth.service.TokenProvider;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.repository.UserRepository;
import server.ourhood.global.auth.annotation.LoginUser;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final TokenProvider tokenProvider;
	private final HeaderHandler headerHandler;
	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginUser.class)
			&& parameter.getParameterType().equals(User.class);
	}

	@Override
	public User resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) {

		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		String accessToken = headerHandler.resolveAccessToken(request);

		if (accessToken == null) {
			return null;
		}

		try {
			tokenProvider.validateAccessToken(accessToken);
			String userId = tokenProvider.getUserIdFromAccessToken(accessToken);
			return userRepository.findById(Long.valueOf(userId)).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}
}
