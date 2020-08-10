package io.damon.snowflakeredisidstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Bwolf
 */
@ConfigurationProperties("damon.snowflake")
public class SnowflakeConfigurationProperties {

    private Long datacenter = 0L;

    public Long getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(Long datacenter) {
        this.datacenter = datacenter;
    }
}
