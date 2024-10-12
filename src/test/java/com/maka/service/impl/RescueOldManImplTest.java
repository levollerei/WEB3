package com.maka.service.impl;

import com.maka.BakaApplication;
import com.maka.BakaApplicationTests;
import com.maka.pojo.OldMan4Two4;
import com.maka.pojo.RescueOldMan;
import com.maka.service.RescueOldManService;
import com.maka.vo.OldManInfoView;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class RescueOldManImplTest extends UserServiceImplTest {



    @Autowired
    private RescueOldManService rescueOldManService;


    @Test
    public void selectRescueOldManByPage() {
        OldMan4Two4 randomOldMan = rescueOldManService.getRandomOldMan();
        System.out.println(randomOldMan);
//        List<RescueOldMan> oldManList = rescueOldManService.selectRescueOldManByPage(1, 10);
//        System.out.println(oldManList);
//
//        List<OldManInfoView> man = rescueOldManService.getPageOldManByCondition(1, 10, null, null, null);
//        System.out.println(man);

    }
}