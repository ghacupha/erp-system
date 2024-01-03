package io.github.erp.internal.service.applicationUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrentUserContextFilterConfig {

    @Autowired
    private CurrentUserClearFilter currentUserClearFilter;

    @Bean
    public FilterRegistrationBean<CurrentUserClearFilter> filterRegistrationBean() {
        FilterRegistrationBean<CurrentUserClearFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(currentUserClearFilter);
        registrationBean.addUrlPatterns("/api/fixed-asset/*"); // Specify the URL pattern to intercept
        return registrationBean;
    }
}

