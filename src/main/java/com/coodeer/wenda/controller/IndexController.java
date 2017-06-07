package com.coodeer.wenda.controller;

import com.coodeer.wenda.dao.UserDAO;
import com.coodeer.wenda.model.User;
import com.coodeer.wenda.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by common on 2017/5/14.
 */
@Controller
public class IndexController {

    @Autowired
    MyService service;

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(){
        return "hello world";
    }

    @RequestMapping(path = "/profile/{username}")
    @ResponseBody
    public String profile(@PathVariable(value = "username") String user){
        StringBuilder ret = new StringBuilder("welcome to my website : ");
        ret.append(user);
        System.out.print(ret);
        return ret.toString();
    }

    @RequestMapping(path = "/showuser")
    @ResponseBody
    public String showUserInfo(@RequestParam(value = "user", required = false, defaultValue = "sir/miss") String user,
                                @RequestParam(value = "sex", defaultValue = "man") String sex){
        StringBuilder ret = new StringBuilder("this is you info : <br>");
        ret.append(user + "<br>" + sex);
        System.out.print(ret);
        return ret.toString();
    }

    @RequestMapping(path = "/vm", method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("string", "this is a string ob from model");
      //  model.addAttribute("user", new User("kobe", 38, "man"));
        Map<String, String> map = new HashMap<>();
        map.put("1", "first ele");
        map.put("2", "second ele");
        map.put("3", "third ele");
        model.addAttribute("map", map);
        return "home";
    }

    @RequestMapping(path = "/parse", method = {RequestMethod.GET})
    public String parse(Model model){
        model.addAttribute("string", "this is a string ob from model");
       // model.addAttribute("user", new User("kobe", 38, "man"));
        return "parseAndInclude";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response){
        StringBuilder sb = new StringBuilder();

        sb.append(request.getMethod() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");
        sb.append(request.getQueryString() + "<br>");

        sb.append("**********************************<br>");
        sb.append("Accept: " + request.getHeader("Accept") + "<br>");
        sb.append("Accept-Encoding: " + request.getHeader("Accept-Encoding") + "<br>");
        sb.append("Host: " + request.getHeader("Host") + "<br>");
        sb.append("Accept-Language: " + request.getHeader("Accept-Language") + "<br>");

        sb.append("**********************************<br>");

        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                sb.append(cookie.getName() + ":" + cookie.getValue() + "<br>");
            }
        }

        sb.append("**********************************<br>");
        Class reqClass = request.getClass();
        for (Method method : reqClass.getMethods()){
            sb.append(method.getName() + "<br>");
        }

        response.addCookie(new Cookie("username", "myname"));

        return sb.toString();
    }

    @ExceptionHandler
    @ResponseBody
    public String exceptionHandler(Exception e){
        return "not find 404" + "<br>detail : <br>" + e.getMessage();
    }

    @RequestMapping("/service")
    @ResponseBody
    public String sercice(){
        return service.doSomeThing();
    }
}
