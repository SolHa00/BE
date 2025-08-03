package server.ourhood.global.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.auth.api.HeaderHandler;
import server.ourhood.domain.auth.application.TokenProvider;
import server.ourhood.global.auth.annotation.PublicApi;
import server.ourhood.global.auth.annotation.SecuredApi;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;
import server.ourhood.global.response.BaseResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final List<String> ALWAYS_EXCLUDE_PATHS = List.of(
		"/api/health"
	);

	static {
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
	}

	private final HeaderHandler headerHandler;
	private final TokenProvider tokenProvider;
	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();

		if (ALWAYS_EXCLUDE_PATHS.stream().anyMatch(path::startsWith)) {
			return true;
		}

		try {
			Object handler = requestMappingHandlerMapping.getHandler(request).getHandler();

			if (handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod)handler;

				if (handlerMethod.getMethod().isAnnotationPresent(SecuredApi.class)) {
					return false;
				}

				if (handlerMethod.getMethod().isAnnotationPresent(PublicApi.class)) {
					return true;
				}

				if (handlerMethod.getBeanType().isAnnotationPresent(PublicApi.class)) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.debug("Error determining handler for request: " + e.getMessage());
		}

		return false;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try {
			if (!shouldSkipFilter(request)) {
				checkTokens(request);
			}
			filterChain.doFilter(request, response);
		} catch (BaseException ex) {
			sendError(response, ex.getStatus());
		}
	}

	private boolean shouldSkipFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/api/auth/") || path.equals("/api/health");
	}

	private void checkTokens(HttpServletRequest request) {
		String accessToken = headerHandler.resolveAccessToken(request);
		tokenProvider.validateAccessToken(accessToken);
	}

	private void sendError(HttpServletResponse response, BaseResponseStatus errorCode) throws IOException {
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		String body = serialize(BaseResponse.fail(errorCode));
		response.getWriter().write(body);
	}

	private String serialize(BaseResponse<Object> responseBody) throws IOException {
		return OBJECT_MAPPER.writeValueAsString(responseBody);
	}
}
