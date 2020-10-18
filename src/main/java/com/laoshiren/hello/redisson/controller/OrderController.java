package com.laoshiren.hello.redisson.controller;

import com.pharmakeyring.health.redisson.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ProjectName:     hello-redisson
 * Package:         com.laoshiren.hello.redisson.controller
 * ClassName:       OrderController
 * Author:          laoshiren
 * Git:             xiangdehua@pharmakeyring.com
 * Description:
 * Date:            2020/8/13 14:26
 * Version:         1.0.0
 */
@Slf4j
@RestController
public class OrderController {

    @Resource
    public RedissonLock redissonLock;

    @GetMapping("order/{itemId}")
    public String getOrder(@PathVariable String itemId) throws Exception {
        // 锁Id 锁有效时间 其他线程等待超时时间
        if (redissonLock.tryLock(itemId, 10L, 13L)) {
            Thread.sleep(12*1000);
            if (redissonLock.isHeldByCurrentThread(itemId)) {
                log.info("解锁 {}  ",Thread.currentThread().getId());
                redissonLock.unlock(itemId);
            }
            return "ok";
        } else {
            log.info("[Executor Redisson ]获取锁 {} 失败",Thread.currentThread().getId());
            return "failed";
        }

    }

}
