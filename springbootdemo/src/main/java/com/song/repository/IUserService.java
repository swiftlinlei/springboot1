package com.song.repository;

import com.song.entity.User;

/**
 * 用户api
 * Create by linlei on 2018/5/26
 */
public interface IUserService {
    /**
     * 根据用户id获取用户信息
     * @param userId 用户微信id
     * @return User 用户对象
     */
    User findUserById(String userId);

    /**
     * 通过微信查询用户id
     * @param code 微信授权code
     * @return String userId
     */
    String findUserIdByWx(String code);
}
