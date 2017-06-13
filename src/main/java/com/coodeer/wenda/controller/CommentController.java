package com.coodeer.wenda.controller;

import com.coodeer.wenda.model.Comment;
import com.coodeer.wenda.model.EntityType;
import com.coodeer.wenda.model.HostHolder;
import com.coodeer.wenda.service.CommentService;
import com.coodeer.wenda.service.QuestionService;
import com.coodeer.wenda.utils.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by common on 2017/6/13.
 */
@Controller
public class CommentController {

    private static final Logger logger = Logger.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                                @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            comment.setContent(content);
            if (hostHolder.getUser() == null) {
                comment.setUserId(MyUtil.ANONYMOUS_USERID);
            } else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            comment.setCreatedDate(new Date());

            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);

        }catch (Exception e){
            logger.error("添加评论失败：" + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
