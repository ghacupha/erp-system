package io.github.erp.config;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
            .antMatchers("/api/logs").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/accounts/**").hasAnyAuthority(
                AuthoritiesConstants.FIXED_ASSETS_USER,
                AuthoritiesConstants.LEASE_MANAGER,
                AuthoritiesConstants.ADMIN,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER,
                AuthoritiesConstants.BOOK_KEEPING)
            .antMatchers("/api/app/fixed-asset/**").hasAnyAuthority(
                AuthoritiesConstants.FIXED_ASSETS_USER,
                AuthoritiesConstants.LEASE_MANAGER,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER)
            .antMatchers("/api/app/files/**").hasAuthority(AuthoritiesConstants.DBA)
            .antMatchers("/api/leases/**").hasAuthority(AuthoritiesConstants.LEASE_MANAGER)
            .antMatchers("/api/docs/**").hasAuthority(AuthoritiesConstants.DOCUMENT_MODULE_USER)
            .antMatchers("/api/design-report/**").hasAuthority(AuthoritiesConstants.REPORT_DESIGNER)
            .antMatchers("/api/read-report/**").hasAuthority(AuthoritiesConstants.REPORT_DESIGNER)
            .antMatchers("/api/read-report/**").hasAuthority(AuthoritiesConstants.REPORT_ACCESSOR)
            .antMatchers("/api/dev/**").hasAuthority(AuthoritiesConstants.DEV)
            .antMatchers("/api/dev-test/**").hasAuthority(AuthoritiesConstants.DEV)
            .antMatchers("/api/app/payments/**").hasAnyAuthority(
                AuthoritiesConstants.LEASE_MANAGER,
                AuthoritiesConstants.PAYMENTS_USER,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER,
                AuthoritiesConstants.REQUISITION_MANAGER,
                AuthoritiesConstants.FIXED_ASSETS_USER)
            .antMatchers("/api/app/requisition/**").hasAnyAuthority(
                AuthoritiesConstants.LEASE_MANAGER,
                AuthoritiesConstants.PAYMENTS_USER,
                AuthoritiesConstants.REQUISITION_MANAGER)
            .antMatchers("/api/app/prepayments/**").hasAnyAuthority(AuthoritiesConstants.PREPAYMENTS_MODULE_USER)
            .antMatchers("/api/taxes/**").hasAuthority(AuthoritiesConstants.TAX_MODULE_USER)
            .antMatchers("/api/granular-data/**").hasAnyAuthority(
                AuthoritiesConstants.GRANULAR_REPORTS_USER,
                AuthoritiesConstants.LEASE_MANAGER,
                AuthoritiesConstants.ADMIN,
                AuthoritiesConstants.PREPAYMENTS_MODULE_USER,
                AuthoritiesConstants.FIXED_ASSETS_USER)
            .antMatchers("/api/**").authenticated()
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
