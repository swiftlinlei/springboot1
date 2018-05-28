package com.song.controller;

import com.song.entity.User;
import com.song.repository.IWeiXinServer;
import com.song.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Song on 2017/2/15.
 * User控制层
 */
@Controller
@RequestMapping(value = "/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private IWeiXinServer weiXinServer;

    @RequestMapping(value = "/index")
    public String index(){
        return "user/index";
    }

    @RequestMapping(value = "/show")
    @ResponseBody
    public String show(@RequestParam(value = "id")String id){
        User user = userService.findUserById(id);
        if(null != user)
            return user.getId()+"/"+user.getLoginid()+"/"+user.getLastname()+"/";
        else return "null";
    }

    @RequestMapping(value = "/getcode")
    @ResponseBody
    @CrossOrigin
    public String getCode(@RequestParam(value = "code",required = false)String code){  //企业微信跳转该api并携带code参数
        //获取access_token
        String accessToken = weiXinServer.getAccessToken(IWeiXinServer.CORP_ID_1,IWeiXinServer.SECRET_1_1);
        //根据access_token和code获取用户id
        String userId = weiXinServer.getUserId(accessToken,code);
        //access_token过期失效处理
        if("access_token已过期".equals(userId)){
            Map<String,String> accessTokenMap = weiXinServer.getAccessTokenMap();
            accessTokenMap.remove(IWeiXinServer.SECRET_1_1);//移除过期的access_token缓存
            accessToken = weiXinServer.getAccessToken(IWeiXinServer.CORP_ID_1,IWeiXinServer.SECRET_1_1);//重新获取
            userId = weiXinServer.getUserId(accessToken,code);
        }
        User user = userService.findUserById(userId);
        if(null != user){
            return "{\"userid\":\""+user.getId()+"\",\"workcode\":\""+user.getLoginid()+"\",\"username\":\""+user.getLastname()+"\"}";
        }
        else return "{\"userid\":\"id自动查询失败\",\"workcode\":\"工号自动查询失败\",\"username\":\"请联系管理员\"}";
    }
}
