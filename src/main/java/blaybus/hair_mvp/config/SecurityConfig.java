package blaybus.hair_mvp.config;


import blaybus.hair_mvp.auth.RequestMatcherHolder;
import blaybus.hair_mvp.auth.jwt.JwtAuthenticationFilter;
import blaybus.hair_mvp.domain.user.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RequestMatcherHolder requestMatcherHolder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(requestMatcherHolder.getRequestMatchersByMinRole(null))
                                .permitAll()
                                .requestMatchers(requestMatcherHolder.getRequestMatchersByMinRole(Role.CLIENT))
                                .hasAnyAuthority(Role.CLIENT.name())
                                .requestMatchers(requestMatcherHolder.getRequestMatchersByMinRole(Role.DESIGNER))
                                .hasAnyAuthority(Role.DESIGNER.name())
                                // kakao 테스트
                                .requestMatchers("/online/v1/payment/ready","/online/v1/payment/approve")
                                .permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();

    }
}