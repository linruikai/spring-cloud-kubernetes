package org.example.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class VersionHeaderFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String version = exchange.getRequest().getHeaders().getFirst("version");
        if (Objects.isNull(version)) {
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("version", "default")
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        }
        return chain.filter(exchange);
    }
}

