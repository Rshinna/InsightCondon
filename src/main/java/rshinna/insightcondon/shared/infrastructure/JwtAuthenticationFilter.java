package rshinna.insightcondon.shared.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXO_TOKEN = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(PREFIXO_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(PREFIXO_TOKEN.length());

        if (jwtService.tokenValido(token)) {
            UUID usuarioId = jwtService.extrairUsuarioId(token);
            String perfil = jwtService.extrairPerfil(token);

            var authentication = new UsernamePasswordAuthenticationToken(
                    usuarioId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + perfil))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
