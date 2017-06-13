package com.coodeer.wenda.controller;

import com.coodeer.wenda.dao.UserDAO;
import com.coodeer.wenda.model.*;
import com.coodeer.wenda.service.CommentService;
import com.coodeer.wenda.service.QuestionService;
import com.coodeer.wenda.service.UserService;
import com.coodeer.wenda.utils.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    CommentService commentService;

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

            if (hostHolder.getUser() == null){
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
    public String getQuestion(Model model, @PathVariable("id") int qid){
        Question question = questionService.getQuestion(qid);
        User user = userService.getUser(question.getUserId());
        model.addAttribute("question", question);
        model.addAttribute("user", user);

        List<Comment> comments = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vo = new ArrayList<>();

        for (Comment comment : comments){
            ViewObject viewObject = new ViewObject();
            viewObject.set("comment", comment);
            viewObject.set("user", userService.getUser(comment.getUserId()));
            vo.add(viewObject);
        }
        model.addAttribute("comments", vo);

        return "detail";
    }

}
