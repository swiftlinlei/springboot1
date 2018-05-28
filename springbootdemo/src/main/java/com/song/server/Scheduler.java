package com.song.server;

import com.song.repository.IWeiXinServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 定时器类
 * Create by linlei on 2018/5/25
 */
@Component
public class Scheduler {
    /**引入日志*/
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IWeiXinServer weiXinServer;
    /**
     * 定时刷新企业微信的accessToken令牌
     * 采用spring boot 定时器注解的方式自动执行
     * 方法为在入口类添加@EnableScheduling注解,在定时方法加@Scheduled
     */
    @Scheduled(cron="0 0 0/1 * * ?")//每小时执行一次
    //Schedule可使用cron表达式或者fixedRate毫秒
    public void refreshAccessToken() {
      //查询缓存了哪些应用的令牌
        Map<String,String> accessTokenMap = weiXinServer.getAccessTokenMap();
        if(accessTokenMap==null){
            return;
        }else{
            for(Map.Entry<String,String> entry:accessTokenMap.entrySet()){
                String corpSecret = entry.getKey();//应用的凭证秘钥
                String accessToken = entry.getValue();//应用所属的令牌
                //重新访问对令牌进行刷新
                String newAccessToken = weiXinServer.getAccessToken(IWeiXinServer.CORP_ID_1,corpSecret);
                accessTokenMap.put(corpSecret,newAccessToken);
                log.info("令牌："+corpSecret+"----"+newAccessToken);
            }
        }
    }
}
