package com.sso.app1.client.service.tmpl;



import com.sso.app1.client.service.UserAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserAccessServiceImpl implements UserAccessService{
    public static final String USER_KEY = "userMap";
	
	public static final String TOKEN_KEY = "tokenMap";
	
	public static final String TOKEN = "TOKEN";
	
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	// @Autowired
	// private RedisTemplate<String, Serializable> redisCacheTemplateS;

	@Override
	public String getUserToken(String user) {
		HashOperations<String, String, String> hashOp = redisTemplate.opsForHash();
		String token = hashOp.get(USER_KEY, user);
		if(token==null) {
			return null;
		}else {
			return TOKEN.equals(hashOp.get(TOKEN_KEY, token)) ? token : null;
		}
	}

	@Override
	public void putUserStatus(String user, String ssoToken) {
		HashOperations<String, String, String> hashOp = redisTemplate.opsForHash();
		hashOp.put(USER_KEY,user,ssoToken);
		hashOp.put(TOKEN_KEY,ssoToken,TOKEN);
	}
	
	@Override
	public void deleteToken(String token) {
		HashOperations<String, String, String> hashOp = redisTemplate.opsForHash();
		hashOp.delete(TOKEN_KEY,token);		
	}

}