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

/**
 * @author Bwolf
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnClass({SnowflakeIdWorker.class})
@EnableConfigurationProperties({SnowflakeConfigurationProperties.class})
public class SnowflakeAutoConfiguration {

    /**
     * redis存储最后已有的workerId.
     */
    private static final String ID_REDIS_KEY = "SNOWFLAKE:LAST_WORKERID";

    @Autowired
    private SnowflakeConfigurationProperties propertie;

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        // 如果分布式redis集群没有此key, 则 0 -> 1
        Long workerId = redisTemplate.opsForValue().increment(ID_REDIS_KEY, 1);
        // 确保workerId 最大为 31
        workerId = workerId & 31;
        return new SnowflakeIdWorker(workerId, propertie.getDatacenter());
    }

}
