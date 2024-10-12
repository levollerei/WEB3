package com.maka.controller;


import com.maka.component.MessageResponse;
import com.maka.mapper.RescueOldManMapper;
import com.maka.pojo.Clue;
import com.maka.pojo.RescueOldMan;
import com.maka.pojo.User;
import com.maka.query.PageRequest;
import com.maka.query.PageResponse;
import com.maka.query.UserInfo;
import com.maka.service.MessageService;
import com.maka.service.RescueOldManService;
import com.maka.service.UserService;
import com.maka.vo.OldManInfoView;
import com.maka.vo.RescuingUser;
import com.maka.vo.TimeLine;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author yang
 */
@CrossOrigin
@RestController
public class RescueOldManController {
    private Logger logger = LoggerFactory.getLogger(RescueOldManController.class);
    private RescueOldManService rescueOldManService;
    private UserService userService;

    public RescueOldManController(RescueOldManService rescueOldManService) {
        this.rescueOldManService = rescueOldManService;
    }

    @Autowired
    public void setMessageService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/getPageRescueOldMan")
    public PageResponse getOldManByPage(PageRequest pageRequest) {
        List<OldManInfoView> oldManInfoViews = rescueOldManService.selectRescueOldManByPage(pageRequest.getCurrentPage(), pageRequest.getPageSize());
        Integer oldManNum = rescueOldManService.getRescueOldManNum();
        pageRequest.setTotalNum(oldManNum);
        pageRequest.setData(oldManInfoViews);
        pageRequest.setPageTotalNum(oldManNum / pageRequest.getPageSize() + 1);
        logger.info("{}", oldManInfoViews);
        return new PageResponse(HttpStatus.OK.value(), "查询成功", oldManNum, oldManInfoViews);
    }


    /**
     * 搜索老人
     *
     * @param pageRequest 分页请求
     * @param name        老人名字
     * @param lostPlace   走失地点
     * @param phone       联系人手机号
     * @return 符合条件的老人列表
     */
    @RequestMapping("/getPageOldManByCondition")
    public PageResponse getPageUserByCondition(PageRequest pageRequest, String name, String lostPlace, String phone) {
        if (StringUtils.isEmptyOrWhitespaceOnly(lostPlace)) {
            lostPlace = null;
        }
        if (StringUtils.isEmptyOrWhitespaceOnly(phone)) {
            phone = null;
        }
        if (StringUtils.isEmptyOrWhitespaceOnly(name)) {
            name = null;
        }
        logger.info("name  = {}", name);
        logger.info("gender = {}", lostPlace);
        logger.info("phone= {}", phone);
        List<OldManInfoView> oldManInfoViews = rescueOldManService.getPageOldManByCondition(pageRequest.getCurrentPage(), pageRequest.getPageSize(), name, lostPlace, phone);
        int totalNum = oldManInfoViews.size();
        return new PageResponse(HttpStatus.OK.value(), "查询成功", totalNum, oldManInfoViews);
    }


    @PostMapping("/updateOldManInfo")
    public MessageResponse updateOldManInfo(OldManInfoView oldManInfoView) {
        return rescueOldManService.updateOldManInfo(oldManInfoView);
    }


    @PostMapping("/getDetailOldManRescue")
    public MessageResponse getDetailOldManRescue(String oldManId, String oldManName) throws ParseException {
        oldManId = "1";
        List<RescuingUser> rescueMan = userService.getUsersToRescueMan(Integer.parseInt(oldManId));

        List<Clue> clueOfOldMan = rescueOldManService.getClueOfOldMan(Integer.parseInt(oldManId));

        ArrayList<TimeLine> timeLines = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

        for (RescuingUser rescuingUser : rescueMan) {
            timeLines.add(rescuingUser.transferToTimeLine());
        }
        for (Clue clue : clueOfOldMan) {
            TimeLine timeLine = clue.transferToTimeLine(oldManName);
            timeLine.setTime(new Date());
            timeLines.add(timeLine);
        }
//        TimeLine timeLine = new TimeLine();
//        String time1 = "2021-4-16 09:02:10";
//        timeLine.setTime(simpleDateFormat.parse(time1));
//        timeLine.setDesc("杨双月在拜伦广场附近没有发现老人");
//        timeLines.add(timeLine);
//
//        time1 = "2021-4-16 09:40:10";
//        TimeLine timeLine2 = new TimeLine();
//        timeLine.setTime(simpleDateFormat.parse(time1));
//        timeLine.setDesc("杨双月在犀浦街道附近没有发现老人");
//        timeLines.add(timeLine2);
//
//        time1 = "2021-4-16 10:20:10";
//        TimeLine timeLine3 = new TimeLine();
//        timeLine.setTime(simpleDateFormat.parse(time1));
//        timeLine.setDesc("杨双月在郫都区小龙坎附近发现疑似老人");
//        timeLines.add(timeLine3);

        timeLines.sort((o1, o2) ->{return (int) (o1.getTime().getTime() - o2.getTime().getTime());});
        return MessageResponse.success("成功", timeLines);

    }


}
