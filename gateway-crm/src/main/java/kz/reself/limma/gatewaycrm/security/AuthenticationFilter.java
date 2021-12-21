package kz.reself.limma.gatewaycrm.security;

import kz.reself.limma.gatewaycrm.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter {
    final Logger logger =
            LoggerFactory.getLogger(AuthenticationFilter.class);
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {
        logger.info("Global Pre Filter executed");
        Object authKey = "Authorization";
        if (exchange.getRequest().getHeaders().containsKey(authKey)) {
            String token = exchange.getRequest().getHeaders().get(authKey).get(0);
            token = token.replace("Bearer", "").trim();
            return chain.filter(exchange.mutate().request(
                    exchange.getRequest().mutate()
                            .header("username", jwtUtil.getUsernameFromToken(token))
                            .build())
                    .build());
        }
        return chain.filter(exchange);

    }
}
