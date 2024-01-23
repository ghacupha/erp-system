package io.github.erp.internal;

import io.github.erp.config.AppPropertyFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "erp-index")
@PropertySource(value = "classpath:config/erpConfigs.yml", factory = AppPropertyFactory.class)
public class IndexProperties {

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
