package com.coodeer.wenda.service;

import com.coodeer.wenda.dao.MessageDAO;
import com.coodeer.wenda.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by common on 2017/6/13.
 */
@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveWordsFilterService sensitiveWordsFilterService;

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveWordsFilterService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? 1 : 0;
    }


    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }


    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConvesationUnreadCount(int userId, String conversationId){
        return messageDAO.getConvesationUnreadCount(userId, conversationId);
    }
}
