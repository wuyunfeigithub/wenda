package com.coodeer.wenda.service;

import com.coodeer.wenda.controller.HomeController;
import com.coodeer.wenda.dao.LoginTicketDAO;
import com.coodeer.wenda.dao.UserDAO;
import com.coodeer.wenda.model.LoginTicket;
import com.coodeer.wenda.model.User;
import com.coodeer.wenda.utils.MyUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.Ticket;

import java.util.*;

/**
 * Created by common on 2017/5/24.
 */
@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }

    public Map<String, String> login(String username, String password){
        Map<String, String> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空！");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg", "该用户名不存在！");
            return map;
        }

        if(!MyUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg", "密码错误！");
            return map;
        }

        String ticket = addTicket(user.getId());
        map.put("ticket", ticket);

        return  map;
    }

    public Map<String, String> register(String username, String password){
        Map<String, String> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空！");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg", "该用户名已经存在！");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(MyUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        String ticket = addTicket(user.getId());
        map.put("ticket", ticket);

        return  map;
    }

    public String addTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date now =  new Date();
        now.setTime(now.getTime() + (long) 1000 * 3600 * 24 * 30);
        logger.info(now);
        ticket.setExpired(now);
        ticket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        ticket.setStatus(0);
        loginTicketDAO.addTicket(ticket);

        return ticket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
