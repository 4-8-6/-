package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("car")
@Data
public class Car {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userid;
    private Integer bookid;
    private Integer num;
}
