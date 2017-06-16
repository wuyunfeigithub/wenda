package com.coodeer.wenda.service;

import com.coodeer.wenda.dao.CommentDAO;
import com.coodeer.wenda.model.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by common on 2017/6/13.
 */
@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveWordsFilterService sensitiveWordsFilterService;

    public List<Comment> getCommentsByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public int addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveWordsFilterService.filter(comment.getContent()));
        return commentDAO.addComment(comment) > 0 ? 1 : 0;
    }

    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public int deleteComment(int entityId, int entityType){
        return commentDAO.updateStatus(entityId, entityType, 1) > 0 ? 1 : 0;
    }

    public Comment getCommentById(int commentId){
        return commentDAO.getCommentById(commentId);
    }
}
