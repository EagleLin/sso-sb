package com.sso.server.config;


import java.io.Serializable;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
 
    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}

// @Configuration
// @AutoConfigureAfter(RedisAutoConfiguration.class)
// public class RedisConfig {

//     /**
//      * 配置自定义redisTemplate
//      * @return
//      */
//     @Bean("TokenSessionTemplate")
//     RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

//         RedisTemplate<String, Object> template = new RedisTemplate<>();
//         template.setConnectionFactory(redisConnectionFactory);

//         //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
//     //  Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
//     Jackson2JsonRedisSerializer<Object> serializer=  new Jackson2JsonRedisSerializer<Object>(Object.class);
//         ObjectMapper mapper = new ObjectMapper();
//         mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//         mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//         serializer.setObjectMapper(mapper);

//         template.setValueSerializer(serializer);
//         //使用StringRedisSerializer来序列化和反序列化redis的key值
//         template.setKeySerializer(new StringRedisSerializer());
//         template.setHashKeySerializer(new StringRedisSerializer());
//         template.setHashValueSerializer(serializer);
//         template.afterPropertiesSet();
//         return template;
//     }

// }