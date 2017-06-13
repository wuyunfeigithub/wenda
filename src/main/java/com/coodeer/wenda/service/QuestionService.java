package com.coodeer.wenda.service;

import com.coodeer.wenda.dao.QuestionDAO;
import com.coodeer.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by common on 2017/5/24.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveWordsFilterService sensitiveWordsFilterService;

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public int addQuestion(Question question){
        //敏感词过滤
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(sensitiveWordsFilterService.filter(question.getTitle()));
        question.setContent(sensitiveWordsFilterService.filter(question.getContent()));

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public Question getQuestion(int id){
        return questionDAO.getById(id);
    }

    public int updateCommentCount(int id, int count){
        return questionDAO.updateCommentCount(id, count);
    }
}
