package com.song.server;

import com.song.entity.User;
import com.song.repository.IUserService;
import com.song.repository.IWeiXinServer;
import com.song.repository.UserRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Song on 2017/2/15.
 * User业务逻辑
 */
@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepositoty userRepositoty;
    @Autowired
    private IWeiXinServer weiXinServer;

    @Override
    public User findUserById(String id){
        User user = null;
        try{
            user = userRepositoty.findByUserId(id);
        }catch (Exception e){}
        return user;
    }

    @Override
    public String findUserIdByWx(String code) {
        return null;
    }
}
