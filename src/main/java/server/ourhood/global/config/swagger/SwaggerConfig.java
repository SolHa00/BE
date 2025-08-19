package server.ourhood.global.config.swagger;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

	private static final String SERVER_NAME = "OurHood";
	private static final String GITHUB_URL = "https://github.com/SolHa00/BE";
	private static final String SWAGGER_API_TITLE = "OurHood 프로젝트 API 문서";
	private static final String SWAGGER_API_DESCRIPTION = "OurHood 프로젝트 API 문서입니다.";

	@Value("${swagger.version:0.0.1}")
	private String version;

	@Value("${swagger.server-url:http://localhost:8080}")
	private String serverUrl;

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.servers(swaggerServers())
			.components(authSetting())
			.info(swaggerInfo())
			.addSecurityItem(new SecurityRequirement().addList("accessToken"));
	}

	private List<Server> swaggerServers() {
		Server server = new Server().url(serverUrl).description(SWAGGER_API_DESCRIPTION);
		return List.of(server);
	}

	private Components authSetting() {
		return new Components()
			.addSecuritySchemes(
				"accessToken",
				new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.in(SecurityScheme.In.HEADER)
					.name("Authorization"));
	}

	private Info swaggerInfo() {
		License license = new License();
		license.setUrl(GITHUB_URL);
		license.setName(SERVER_NAME);

		return new Info()
			.title(SWAGGER_API_TITLE)
			.version("v" + version)
			.description(SWAGGER_API_DESCRIPTION)
			.license(license);
	}

	@Bean
	public ModelResolver modelResolver(ObjectMapper objectMapper) {
		return new ModelResolver(objectMapper);
	}
}
