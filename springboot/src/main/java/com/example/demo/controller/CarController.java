package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Car;
import com.example.demo.mapper.CarMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Resource
    CarMapper carMapper;
    @PostMapping
    public Result<?> save(@RequestBody Car car)
    {
        car.setNum(1);
        QueryWrapper<Car> qw = new QueryWrapper<>();
        qw.eq("userid",car.getUserid()).eq("bookid",car.getBookid());
        Car c = carMapper.selectOne(qw);
        if(c == null)
        {
            carMapper.insert(car);
        }
        else
        {
            c.setNum(c.getNum()+1);
            carMapper.updateById(c);
        }
        return Result.success();
    }
    @PutMapping
    public Result<?> update(@RequestBody Car car)
    {
        carMapper.updateById(car);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id)
    {
        carMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping("/{userid}")
    public Result<?> select(@PathVariable Integer userid)
    {
        QueryWrapper<Car> qw = new QueryWrapper<>();
        qw.eq("userid",userid);
        List<Car> list = carMapper.selectList(qw);
        return Result.success(list);
    }
}


