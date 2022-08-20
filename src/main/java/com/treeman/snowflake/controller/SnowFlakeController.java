package com.treeman.snowflake.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.treeman.snowflake.exception.SeqException;
import com.treeman.snowflake.pojo.Snowflake;
import com.treeman.snowflake.server.ISnowFlakeServer;
import com.treeman.snowflake.tool.SnowFlakeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SnowFlakeController {

    @Autowired
    SnowFlakeTool genTool;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    ISnowFlakeServer snowFlakeServer;

    private String REDIS_KEY_NAME   = "snowflakeList";

    @GetMapping()
    public ModelAndView toIndex(){
        List<Snowflake> list;
        if (redisTemplate.hasKey(REDIS_KEY_NAME)) {
            list = JSONArray.parseArray(redisTemplate.opsForValue().get(REDIS_KEY_NAME),Snowflake.class);
        }else{
            list = snowFlakeServer.selectIDList();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("snowflakes",list);
        modelAndView.setViewName("index");
        return modelAndView;
    }
    @GetMapping("/add")
    public ModelAndView getId() throws SeqException {
        long l = genTool.nextNo();
        String s = String.valueOf(l);
        Snowflake snowflake = split(l);
        snowFlakeServer.insertID(snowflake);   //存入数据库
        redisTemplate.opsForValue().set(String.valueOf(snowflake.getId()), JSON.toJSONString(snowflake)); //存入redis缓存
        redisTemplate.opsForValue().set(REDIS_KEY_NAME,JSONArray.toJSONString(snowFlakeServer.selectIDList())); //存入redis总的列表
        List<Snowflake> list= snowFlakeServer.selectIDList();ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("snowflakes",list);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView getList(@RequestParam("id") long id){
        List<Snowflake> list = new ArrayList<>(16);
        if (redisTemplate.hasKey(String.valueOf(id))) {  //暂时只查内存有的
            Snowflake snowflake = JSON.parseObject(redisTemplate.opsForValue().get(String.valueOf(id)),Snowflake.class);
            list.add(snowflake);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("snowflakes",list);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/delete")
    public ModelAndView deleteID(@RequestParam("id") long id){
        String msg="";
        if (redisTemplate.hasKey(String.valueOf(id))) {
            redisTemplate.delete(String.valueOf(id));
            snowFlakeServer.deleteID(id);
            msg= "已成功删除";
        }else{
            if (snowFlakeServer.deleteID(id)) {
                msg= "删除成功";
            }else{
                msg= "无此数据";
            }
        }
        redisTemplate.opsForValue().set(REDIS_KEY_NAME,JSONArray.toJSONString(snowFlakeServer.selectIDList())); //存入redis总的列表
        List<Snowflake> list= snowFlakeServer.selectIDList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("snowflakes",list);
        modelAndView.setViewName("index");
        return modelAndView;
    }

/* *
 * @author treeman
 * @date 2022/8/19 19:26
 * @param [l] 自增ID
 * @return Snowflake 返回雪花ID切割好的类信息
 */
    private Snowflake split(long l) {
        String s = Long.toBinaryString(l);
        StringBuilder s1 = new StringBuilder("");
        for(int i=s.length();i<=64;i++){ //补全前面的0
            s1.append("0");
        }
        s=s1.toString()+s;
        Long time = Long.parseLong(s.substring(1,43),2);
        Long work = Long.parseLong(s.substring(43,48),2);
        Long data = Long.parseLong(s.substring(48,53),2);
        Long sequence = Long.parseLong(s.substring(53),2);
        Snowflake snowflake = new Snowflake();
        snowflake.setId(l);
        snowflake.setTimeBit(time);
        snowflake.setWorkBit(work);
        snowflake.setDataBit(data);
        snowflake.setSequence(sequence);
        return snowflake;
    }
}
