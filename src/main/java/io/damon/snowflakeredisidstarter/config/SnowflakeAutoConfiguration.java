package io.damon.snowflakeredisidstarter.config;

import io.damon.snowflakeredisidstarter.idworker.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({SnowflakeIdWorker.class})
@EnableConfigurationProperties({SnowflakeConfigurationProperties.class})
public class SnowflakeAutoConfiguration {

    private static final String ID_REDIS_KEY = "SNOWFLAKE:LAST_WORKERID";

    @Autowired
    private SnowflakeConfigurationProperties propertie;

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        Long workerId = redisTemplate.opsForValue().increment(ID_REDIS_KEY, 1);
        workerId = workerId & 31;
        return new SnowflakeIdWorker(workerId, propertie.getDatacenter());
    }

}
