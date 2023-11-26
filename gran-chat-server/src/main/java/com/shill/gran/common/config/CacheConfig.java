package com.shill.gran.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean("CaffeineCacheManager")
    @Primary
    public CacheManager CaffeineCacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        //常用
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().
                expireAfterWrite(5, TimeUnit.MINUTES)
                .initialCapacity(100).maximumSize(200));
        return caffeineCacheManager;
    }
}
