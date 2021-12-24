package com.example.demo.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.News;
import com.example.demo.mapper.NewsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
//返回json数据的controller
//配置路由
@RequestMapping("/news")
public class NewsController
{

    @Resource
    NewsMapper newsMapper;
    //后端可以接受前台传入的用户数据
    //转成Book类的数据，并且进行insert
    //前台需要工具axios
    @PostMapping
    public Result<?> save(@RequestBody News news)
    {
        news.setTime(new Date());
        newsMapper.insert(news);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody News news)
    {
        newsMapper.updateById(news);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id)
    {
        newsMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    {

       Page<News> page =  newsMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<News>lambdaQuery().like(News::getTitle,search));
        return Result.success(page);
    }
}
