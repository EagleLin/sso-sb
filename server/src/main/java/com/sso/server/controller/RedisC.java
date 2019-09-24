package com.sso.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisC{
    // @Autowired
	// private RedisOperatorService redisOperatorService;
    // @Autowired
    // private StringRedisTemplate stringRedisTemplate;
 
    // //添加
    // @GetMapping(value="/redisAdd")
    // public void saveRedis(){
    //     System.out.println("dfdfdf");
    //     stringRedisTemplate.opsForValue().set("a","test");
    // }
    //  //获取
    //  @GetMapping(value="/redisGet")
    //  public String getRedis(){
    //      return stringRedisTemplate.opsForValue().get("a");
    //  }
  
     @Autowired
     @RequestMapping("/set")
     public String setPOJO(){
        //  TokenSession user = new TokenSession("testtoken","testName");
        //  redisOperatorService.putTokenInfo("user1", user);
         return "存储对象";
     }
  
     @RequestMapping("/get")
     public String getPOJO() {
        // redisOperatorService.getTokenInfo("user1");
        return "result";
     }
 
}