package com.wqs.root.controller;

import com.github.pagehelper.Page;
import com.wqs.root.domain.User;
import com.wqs.root.service.UserService;
import com.wqs.root.util.MyPage;
import com.wqs.root.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @RequestMapping("/user")
    @ResponseBody
    //@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,@RequestParam(value = "pageSize",defaultValue = "5")int pageSize
    public MyPage<User> findUserPage(@RequestBody(required = false) MyPage myPage){
        Page<User> persons = userService.findUserPage(myPage.getPageNumber(), myPage.getPageSize());
        MyPage<User> myPages=new MyPage<User>(persons);
        return  myPages;
    }
    @RequestMapping(value = "/redis",method = RequestMethod.GET)
    @ResponseBody
    public String redisTest(){
        Page<User> persons = userService.findUserPage(1, 5);
        redisUtil.set("persons",persons);
        System.out.println("进入了方法");
        String persons1 = redisUtil.get("persons").toString();
        System.out.println(persons1);
        return persons1;
    }
    @RequestMapping("add")
    public void add(){
        for(int i=16;i<100;i++){
            User user = new User();
            user.setName("葫芦娃"+i);
            userService.addUser(user);
        }
        System.out.println("执行完成!!!");
    }
    @RequestMapping("query")
    @ResponseBody
    public List<User> query(){
        return userService.queryAllUser();
    }
}
