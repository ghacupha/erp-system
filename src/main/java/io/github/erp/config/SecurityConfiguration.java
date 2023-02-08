package io.github.erp.config;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.security.AuthoritiesConstants;
import io.github.erp.security.jwt.JWTConfigurer;
import io.github.erp.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JHipsterProperties jHipsterProperties;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(
        TokenProvider tokenProvider,
        CorsFilter corsFilter,
        JHipsterProperties jHipsterProperties,
        SecurityProblemSupport problemSupport
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/swagger-ui/**").antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .headers()
            .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
        .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        .and()
            .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
        .and()
            .frameOptions()
            .deny()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            // .antMatchers("/api/**").hasAuthority(AuthoritiesConstants.DEV)
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/users/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/authorities/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/account").permitAll()
            .antMatchers("/api/account/change-password").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            // .antMatchers("/api/account").hasAnyAuthority(AuthoritiesConstants.ANONYMOUS)
            .antMatchers("/api/account/reset-password/finish").permitAll()
//            .antMatchers("/api/account").hasAnyAuthority(
//                AuthoritiesConstants.ADMIN,
//            AuthoritiesConstants.DEV)
            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api/accounts/**").hasAnyAuthority(
                AuthoritiesConstants.FIXED_ASSETS_USER,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER,
                AuthoritiesConstants.BOOK_KEEPING)
            .antMatchers("/v2/api/fixed-asset/**").hasAnyAuthority(
                AuthoritiesConstants.FIXED_ASSETS_USER,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER)
            .antMatchers("/v2/api/files/**").hasAuthority(AuthoritiesConstants.DBA)
            .antMatchers("/v2/api/docs/**").hasAuthority(AuthoritiesConstants.DOCUMENT_MODULE_USER)
            .antMatchers("/v2/api/design-report/**").hasAuthority(AuthoritiesConstants.REPORT_DESIGNER)
            .antMatchers("/v2/api/read-report/**").hasAnyAuthority(AuthoritiesConstants.REPORT_DESIGNER, AuthoritiesConstants.REPORT_ACCESSOR)
            .antMatchers("/v2/api/dev/**").hasAuthority(AuthoritiesConstants.DEV)
            .antMatchers("/v2/api/dev-test/**").hasAuthority(AuthoritiesConstants.DEV)
            .antMatchers("/v2/api/payments/**").hasAnyAuthority(
                AuthoritiesConstants.PAYMENTS_USER,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER,
                AuthoritiesConstants.REQUISITION_MANAGER,
                AuthoritiesConstants.FIXED_ASSETS_USER)
            .antMatchers("/v2/api/requisition/**").hasAnyAuthority(
                AuthoritiesConstants.PAYMENTS_USER,
                AuthoritiesConstants.REQUISITION_MANAGER)
            .antMatchers("/v2/api/prepayments/**").hasAnyAuthority(AuthoritiesConstants.PREPAYMENTS_MODULE_USER)
            .antMatchers("/v2/api/taxes/**").hasAuthority(AuthoritiesConstants.TAX_MODULE_USER)
            .antMatchers("/v2/api/granular-data/**").hasAnyAuthority(
                AuthoritiesConstants.GRANULAR_REPORTS_USER,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER,
                AuthoritiesConstants.FIXED_ASSETS_USER)
            //.antMatchers("/api/**").authenticated() // TODO confirm this works!!!
            .antMatchers("/api/**").hasAuthority(AuthoritiesConstants.DEV)
            .antMatchers("/websocket/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/health/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .httpBasic()
        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
