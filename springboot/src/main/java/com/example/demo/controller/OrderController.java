package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderMapper orderMapper;
    @PostMapping
    public Result<?> save(@RequestBody Order order){
        order.setDate(new Date());
        orderMapper.insert(order);
        return  Result.success();
    }
    @GetMapping("/{userid}")
    public Result<?> select(@PathVariable Integer userid){
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("userid",userid);
        List<Order> list = orderMapper.selectList(qw);
        return Result.success(list);
    }
    @GetMapping
    public Result<?> selectall()
    {
        QueryWrapper<Order> qw = new QueryWrapper<>();
        List<Order> list = orderMapper.selectList(qw);
        return Result.success(list);
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id)
    {
        orderMapper.deleteById(id);
        return Result.success();
    }
}
