package oc.moneylog.server.infrastructure.openapi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun moneylogOpenApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Moneylog API")
                .version("v1")
                .description("머니로그 백엔드 API 명세"),
        )
        .servers(
            listOf(
                Server().url("/").description("default"),
            ),
        )
}
