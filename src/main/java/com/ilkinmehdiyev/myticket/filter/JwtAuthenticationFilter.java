package com.ilkinmehdiyev.myticket.filter;

import com.ilkinmehdiyev.myticket.domain.entity.User;
import com.ilkinmehdiyev.myticket.repo.TokenRepository;
import com.ilkinmehdiyev.myticket.security.CustomUserDetails;
import com.ilkinmehdiyev.myticket.security.JwtService;
import com.ilkinmehdiyev.myticket.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired private JwtService jwtService;

  @Autowired private UserService userService;

  @Autowired private TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String jwtToken = authorization.substring(7);

    try {
      String username = jwtService.extractUsername(jwtToken, true);
      if (username != null && SecurityContextHolder.getContext() == null) {
        User user = userService.findByUsername(username);
        CustomUserDetails userDetails = user.userDetails();

        boolean isValidToken =
            tokenRepository
                .findByValue(jwtToken)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (Boolean.TRUE.equals(isValidToken) && jwtService.isTokenValid(jwtToken, true)) {
          var authenticationToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    filterChain.doFilter(request, response);
  }
}
