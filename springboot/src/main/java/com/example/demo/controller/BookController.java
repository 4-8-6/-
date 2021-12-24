package com.example.demo.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
//返回json数据的controller
//配置路由
@RequestMapping("/book")
public class BookController
{

    @Resource
    BookMapper BookMapper;
    //后端可以接受前台传入的用户数据
    //转成Book类的数据，并且进行insert
    //前台需要工具axios
    @PostMapping
    public Result<?> save(@RequestBody Book book)
    {
        BookMapper.insert(book);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Book book)
    {
        BookMapper.updateById(book);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id)
    {
        BookMapper.deleteById(id);
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    {

       Page<Book> page =  BookMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<Book>lambdaQuery().like(Book::getName,search));
        return Result.success(page);
    }
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id){
        Book bo = BookMapper.selectById(id);
        if(bo == null)
        {
            return  Result.error("-1","没有哦");
        }
        else{
            return Result.success(bo);
        }
    }
}
