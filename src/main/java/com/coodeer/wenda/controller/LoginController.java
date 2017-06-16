package com.coodeer.wenda.controller;

import com.coodeer.wenda.async.EventModel;
import com.coodeer.wenda.async.EventProducer;
import com.coodeer.wenda.async.EventType;
import com.coodeer.wenda.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by common on 2017/5/30.
 */
@Controller
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/reglogin")
    public String reg(Model model,
                       @RequestParam(value = "next", required = false) String next){
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        @RequestParam(value = "next", required = false) String next,
                        HttpServletResponse response){

        try {
            Map<String, String> map = userService.login(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }

                /*
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("username", username).setExt("email", "1097928227@qq.com")
                        .setActorId(Integer.valueOf(map.get("userId"))));
                */

                response.addCookie(cookie);
                if(!StringUtils.isBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/home";
            }
            else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e){
            logger.info("登录失败： " + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = "/reg", method = RequestMethod.POST)
    public String register(Model model,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse response){

        try {
            Map<String, String> map = userService.register(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/home";
            }
            else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e){
            logger.info("注册失败： " + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = "/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/home";
    }
}
