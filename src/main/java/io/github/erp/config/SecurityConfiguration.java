package io.github.erp.config;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
