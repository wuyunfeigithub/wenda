package com.coodeer.wenda.controller;

import com.coodeer.wenda.async.EventModel;
import com.coodeer.wenda.async.EventProducer;
import com.coodeer.wenda.async.EventType;
import com.coodeer.wenda.model.Comment;
import com.coodeer.wenda.model.EntityType;
import com.coodeer.wenda.model.HostHolder;
import com.coodeer.wenda.service.CommentService;
import com.coodeer.wenda.service.LikeService;
import com.coodeer.wenda.utils.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by common on 2017/6/15.
 */
@Controller
public class LikeController {

    private static final Logger logger = Logger.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null){
            return MyUtil.getJSONString(999);
        }
        Comment comment = commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityId(commentId)
                .setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return MyUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return MyUtil.getJSONString(0, String.valueOf(0));
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return MyUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
