package com.song.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.song.repository.IWeiXinServer;
import com.song.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业微信接口实现类
 * Create by linlei on 2018/5/24
 */
@Service
public class WeiXinServerImpl implements IWeiXinServer {
    /**引入日志*/
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /**缓存accessToken*/
    Map<String,String> accessTokenMap = new HashMap<String,String>();

    @Override
    public String getAccessToken(String corpId,String secret) {
        //读取缓存
        String accessToken = accessTokenMap.get(secret);
        if(accessToken ==null){ //无缓存或不使用缓存，重新生成accessToken
            String msg =HttpUtils.clientGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + secret);
            JSONObject obj = JSON.parseObject(msg);
            if(obj!=null&&(Integer)obj.get("errcode")==0) {//成功获取
                accessToken = (String)obj.get("access_token");
                //写入缓存
                cacheAccessToken(secret,accessToken);
            }
        }
        return accessToken;
    }

    @Override
    public void cacheAccessToken(String cropsecret, String accessToken) {
        accessTokenMap.put(cropsecret,accessToken);
    }

    @Override
    public Map<String, String> getAccessTokenMap() {
        return accessTokenMap;
    }

    @Override
    public String getUserId(String accessToken, String code) {
        String userId = null;
        //根据code获取用户信息
        String msg = HttpUtils.clientGet("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code="+code);
        log.info("根据code获取用户信息："+msg);
        //解析信息，获取userid
        JSONObject obj = JSON.parseObject(msg);
        if(obj != null){
            Integer errcode = (Integer)obj.get("errcode");
            if (errcode==0){
                userId = (String)obj.get("UserId");
            }else if(errcode==42001){//access_token已过期
                userId="access_token已过期";
            }
        }
        return userId;
    }


}
