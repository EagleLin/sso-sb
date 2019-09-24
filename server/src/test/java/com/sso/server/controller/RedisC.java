package com.sso.server.controller;

import java.io.Serializable;

import com.sso.server.entity.TokenSession;
import com.sso.server.service.AuthSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisC{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
	private AuthSessionService authSessionService;
    //添加
    @GetMapping(value="/test")
    public void saveRedis(){
        System.out.println("dfdfdf");
        stringRedisTemplate.opsForValue().set("a","test");
    }
    @RequestMapping(value="/varifyToken",method=RequestMethod.GET)
	@ResponseBody
	public String varifyToken(String token, String address) {
		return String.valueOf(authSessionService.checkAndAddAddress(token, address));
    }
    
    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
 
    @RequestMapping("/set")
    public String setPOJO(){
 
        TokenSession user = new TokenSession("testtoken","testName");
        redisTemplate.opsForValue().set("user1", user);
        return "存储对象";
    }
 
    @RequestMapping("/get")
    public Object getPOJO(){
        return redisTemplate.opsForValue().get("user2");
    }
}