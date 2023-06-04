package com.ahmeric.store.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * This class provides the security configuration for the application. It configures the security
 * filter chain and JWT authentication filter.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private static final String LOCALHOST_IPV4 = "127.0.0.1";
  private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

  // 10.0.0.0 - 10.255.255.255
  private static final Pattern IPV4_PRIVATE_RANGE1 = Pattern.compile(
      "^10(\\.(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)){3}$"
  );

  // 172.16.0.0 - 172.31.255.255
  private static final Pattern IPV4_PRIVATE_RANGE2 = Pattern.compile(
      "^172\\.(1[6-9]|2\\d|3[01])(\\.(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)){2}$"
  );

  // 192.168.0.0 - 192.168.255.255
  private static final Pattern IPV4_PRIVATE_RANGE3 = Pattern.compile(
      "^192\\.168(\\.(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)){2}$"
  );

  // IPv6 Private address space
  private static final Pattern IPV6_PRIVATE_RANGE = Pattern.compile(
      "^([fF][cCdD][0-9a-fA-F]2:)+"
  );

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  /**
   * This method configures the Security Filter Chain.
   *
   * @param http the HttpSecurity instance
   * @return the SecurityFilterChain instance
   * @throws Exception if a security configuration error occurs
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .requireCsrfProtectionMatcher(this::isNotLocalhost)
        .and()
        .authorizeHttpRequests()
        .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**",
            "/swagger-ui/**", "/actuator/**")
        .permitAll()
        .requestMatchers("/api/v1/users/**").hasAuthority("EMPLOYEE")
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * This method checks if the request comes from localhost or a local network address. If it does,
   * the request bypasses CSRF protection.
   *
   * <p>This approach is chosen instead of disabling CSRF protection completely,
   * and it might be suitable in a microservices' environment where inter-service communication is
   * trusted.</p>
   *
   * <p>However, it permits any device on your local network to send CSRF-unprotected
   * requests. This might expose the application to network-level attacks and potential spoofing of
   * requests. Always ensure you understand the security risks before applying this method.</p>
   *
   * @param request The incoming request.
   * @return true if the request does not originate from localhost or local network.
   */
  private boolean isNotLocalhost(HttpServletRequest request) {
    String remoteAddr = request.getRemoteAddr();

    boolean isLocalhost = remoteAddr.equals(LOCALHOST_IPV4) || remoteAddr.equals(LOCALHOST_IPV6);
    boolean isPrivateNetworkIp = IPV4_PRIVATE_RANGE1.matcher(remoteAddr).matches()
        || IPV4_PRIVATE_RANGE2.matcher(remoteAddr).matches()
        || IPV4_PRIVATE_RANGE3.matcher(remoteAddr).matches()
        || IPV6_PRIVATE_RANGE.matcher(remoteAddr).matches();

    return !isLocalhost && !isPrivateNetworkIp;
  }
}
