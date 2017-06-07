package com.coodeer.wenda.controller;

import com.coodeer.wenda.dao.UserDAO;
import com.coodeer.wenda.model.HostHolder;
import com.coodeer.wenda.model.Question;
import com.coodeer.wenda.model.User;
import com.coodeer.wenda.service.QuestionService;
import com.coodeer.wenda.service.UserService;
import com.coodeer.wenda.utils.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by common on 2017/6/6.
 */
@Controller
public class QuestionController {

    private static final Logger logger = Logger.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        try {

            Question question = new Question();

            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);

            if (hostHolder == null){
                return MyUtil.getJSONString(999);
            }
            else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question) > 0){
                return MyUtil.getJSONString(0);
            }
        } catch (Exception e){
            logger.error("增加问题失败： " + e.getMessage());
        }
        return MyUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value = {"/question/{id}"}, method = RequestMethod.GET)
    public String getQuestion(Model model, @PathVariable("id") int id){
        Question question = questionService.getQuestion(id);
        User user = userService.getUser(question.getUserId());
        model.addAttribute("question", question);
        model.addAttribute("user", user);

        return "detail";
    }

}
