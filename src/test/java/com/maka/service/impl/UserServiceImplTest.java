package com.maka.service.impl;

import com.maka.pojo.User;
import com.maka.query.UserInfo;
import com.maka.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @org.junit.Test
    public void registerVolunteer() {
    }

    @org.junit.Test
    public void getTotalUsersNums() {
    }

    @org.junit.Test
    public void selectUserByPage() {
        List<UserInfo> users = userService.selectUserByPage(1, 10);
        for(UserInfo user:users){
            System.out.println(user);
        }
    }
}