package com.song.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信接口API
 * Created by linlei on 2018/5/24
 */
public interface IWeiXinServer {
    /**企业id*/
    String CORP_ID_1 = "wx458e451958444efa";
    /**应用secret*/
    String SECRET_1_1 = "1ydjFj_Ilvaa5RUMe9HOkAojrgR2q2Gbn_bJGGVUgrU";
    /**
     * 获取令牌
     * 内部默认访问网址为：https://qyapi.weixin.qq.com/cgi-bin/gettoke?corpid=corpId&corpsecret=secret
     * 内部默认令牌有效时间expires_in返回值为7200s
     * @param corpId 企业id
     * @param secret 应用秘钥
     * @return String access_token 令牌
     */
    String getAccessToken(String corpId,String secret);

    /**
     * 执行更新accessToken的缓存
     * @param cropsecret  应用的凭证秘钥
     * @param accessToken 令牌
     */
    void cacheAccessToken(String cropsecret,String accessToken);

    /**
     * 返回一个缓存access_token的map对象
     * @return map 存储access_token
     */
    Map<String,String> getAccessTokenMap();

    /**
     * 根据code获取成员信息
     * @param accessToken   令牌（2小时有效时间）
     * @param code  授权（5分钟有效时间）
     * @return
     */
    String getUserId(String accessToken,String code);
}
