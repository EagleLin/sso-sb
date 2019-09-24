package com.sso.server.service.tmpl;

import java.io.Serializable;

import com.sso.server.entity.TokenSession;
import com.sso.server.service.RedisOperatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service("RedisOperatorService")
public class RedisOperatorServiceImpl implements RedisOperatorService{
    @Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	public static final String USER_KEY = "userMap";
	
	public static final String TOKEN_KEY = "tokenMap";

	// @Qualifier("TokenSessionTemplate")
	// private RedisTemplate<String, TokenSession> redisTemplateN;
	@Autowired
	private RedisTemplate<String, Serializable> redisCacheTemplateS;
	

	@Override
	public void putUserInfo(String userName, String token) {
		HashOperations<String, String, String> hashUserOp = redisTemplate.opsForHash();
		RedisSerializer<?> redisSerializer = new StringRedisSerializer();
		redisSerializer = new StringRedisSerializer();
		//redisSerializer = new Jackson2JsonRedisSerializer<>(TokenSession.class);
		redisTemplate.setHashValueSerializer(redisSerializer);
		hashUserOp.put(USER_KEY,userName,token);
	}
	
	@Override
	public void deleteUserInfo(String userName) {
		HashOperations<String, String, String> hashUserOp = redisTemplate.opsForHash();
		RedisSerializer<?> redisSerializer = new StringRedisSerializer();
		redisSerializer = new StringRedisSerializer();
		//redisSerializer = new Jackson2JsonRedisSerializer<>(TokenSession.class);
		redisTemplate.setHashValueSerializer(redisSerializer);
		hashUserOp.delete(USER_KEY,userName);
	}

	@Override
	public void putTokenInfo(String tokenKey, TokenSession tokenSession) {
		// HashOperations<String, String, TokenSession> hashTokenOp = redisTemplate.opsForHash();
		// RedisSerializer<?> redisSerializer = new StringRedisSerializer();
		// redisSerializer = new JdkSerializationRedisSerializer();
		// //redisSerializer = new Jackson2JsonRedisSerializer<>(TokenSession.class);
		// redisTemplate.setHashValueSerializer(redisSerializer);
		// hashTokenOp.put(TOKEN_KEY,tokenKey,tokenSession);
		redisCacheTemplateS.opsForValue().set(tokenKey, tokenSession);
	}
	
	@Override
	public void deleteTokenInfo(String tokenKey) {
		// HashOperations<String, String, TokenSession> hashTokenOp = redisTemplate.opsForHash();
		// RedisSerializer<?> redisSerializer = new StringRedisSerializer();
		// redisSerializer = new JdkSerializationRedisSerializer();
		// //redisSerializer = new Jackson2JsonRedisSerializer<>(TokenSession.class);
		// redisTemplate.setHashValueSerializer(redisSerializer);
		// hashTokenOp.delete(TOKEN_KEY,tokenKey);
		redisCacheTemplateS.delete(tokenKey);
		
	}

	@Override
	public String getUserInfo(String userName) {
		HashOperations<String, String, String> hashUserOp = redisTemplate.opsForHash();
		RedisSerializer<?> redisSerializer = new StringRedisSerializer();
		redisSerializer = new StringRedisSerializer();
		//redisSerializer = new Jackson2JsonRedisSerializer<>(TokenSession.class);
		redisTemplate.setHashValueSerializer(redisSerializer);
		return hashUserOp.get(USER_KEY, userName); 
	}

	@Override
	public TokenSession getTokenInfo(String tokenKey) {
		// HashOperations<String, String, TokenSession> hashTokenOp = redisTemplate.opsForHash();
		// RedisSerializer<?> redisSerializer = new StringRedisSerializer();
		// redisSerializer = new JdkSerializationRedisSerializer();
		// //redisSerializer = new Jackson2JsonRedisSerializer<>(TokenSession.class);
		// redisTemplate.setHashValueSerializer(redisSerializer);
		// TokenSession token=hashTokenOp.get(TOKEN_KEY,tokenKey);
		// return token;
		TokenSession token=null;
		Object resObj= redisCacheTemplateS.opsForValue().get(tokenKey);
		if(resObj!=null){
			token=(TokenSession)resObj;
		//	token.setAddressList(resObj.);
		}
		return token;
	}
}