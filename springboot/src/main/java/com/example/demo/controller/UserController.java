package com.example.demo.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;

import javax.annotation.Resource;

@RestController
//返回json数据的controller
//配置路由
@RequestMapping("/user")
public class UserController
{
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user)
    {
        //System.out.println("here");
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
        if(res == null){
            return Result.error("-1","用户名或密码错误");
        }
        return Result.success(res);
    }
    @Resource
    UserMapper userMapper;
    //后端可以接受前台传入的用户数据
    //转成user类的数据，并且进行insert
    //前台需要工具axios
    @PostMapping
    public Result<?> save(@RequestBody User user)
    {
        //System.out.println("here");
        userMapper.insert(user);
        return Result.success();
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user)
    {
        //System.out.println("here");
        user.setRole(1);
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if (res!=null)
        {
            return(Result.error("-1","用户名重复"));
        }
        if(user.getPassword()==null)
        {
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }
    @PutMapping
    public Result<?> update(@RequestBody User user)
    {
        userMapper.updateById(user);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id)
    {
        userMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    {

       Page<User> page =  userMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<User>lambdaQuery().eq(User::getRole,1).like(User::getUsername,search));
        return Result.success(page);
    }
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(userMapper.selectById(id));
    }
}
