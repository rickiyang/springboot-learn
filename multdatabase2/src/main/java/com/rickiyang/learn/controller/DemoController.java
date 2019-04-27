package com.rickiyang.learn.controller;

import com.alibaba.fastjson.JSON;
import com.rickiyang.learn.common.BaseResponse;
import com.rickiyang.learn.common.CodeEnum;
import com.rickiyang.learn.entity.db1.User;
import com.rickiyang.learn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private UserService userService;


    @PostMapping(value = "/add")
    public BaseResponse add(@RequestBody User user) {
        try {
            userService.insert(user);
        } catch (Exception e) {
            return BaseResponse.fail(CodeEnum.SYSTEM_ERROR);
        }
        return BaseResponse.success(CodeEnum.SUCCESS);
    }

    @GetMapping(value = "/getById")
    public BaseResponse getById(@RequestParam(value = "id") long id) {
        User user;
        try {
            user = userService.queryUserById(id);
            log.info("我是一条测试日志");
        } catch (Exception e) {
            return BaseResponse.fail(CodeEnum.SYSTEM_ERROR);
        }
        return BaseResponse.success(JSON.toJSONString(user));
    }


}
