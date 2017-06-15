package com.coodeer.wenda.controller;

import com.coodeer.wenda.model.HostHolder;
import com.coodeer.wenda.model.Message;
import com.coodeer.wenda.model.User;
import com.coodeer.wenda.model.ViewObject;
import com.coodeer.wenda.service.MessageService;
import com.coodeer.wenda.service.UserService;
import com.coodeer.wenda.utils.MyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by common on 2017/6/13.
 */
@Controller
public class MessageController {

    private static final Logger logger = Logger.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try {
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversations = messageService.getConversationList(localUserId, 0, 10);
            List<ViewObject> vo = new ArrayList<>();
            for(Message conversation : conversations){
                ViewObject viewObject = new ViewObject();
                viewObject.set("conversation", conversation);
                int targetUserId = conversation.getFromId() == localUserId ? conversation.getToId() : conversation.getFromId();
                viewObject.set("user", userService.getUser(targetUserId));
                viewObject.set("unread", messageService.getConvesationUnreadCount(localUserId, conversation.getConversationId()));
                vo.add(viewObject);
            }
            model.addAttribute("conversations", vo);
        } catch (Exception e){
            logger.error("获取会话列表失败：" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId){
        try {
            List<Message> messages = messageService.getConversationDetail(conversationId, 0, 10);
            messageService.updateConversationUnreadCount(conversationId);
            List<ViewObject> vo = new ArrayList<>();
            for(Message message : messages){
                ViewObject viewObject = new ViewObject();
                viewObject.set("message", message);
                viewObject.set("user", userService.getUser(message.getFromId()));
                vo.add(viewObject);
            }
            model.addAttribute("messages", vo);
        }
        catch (Exception e){
            logger.error("获取详情失败：" + e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                            @RequestParam("content") String content){
        try {

            if(hostHolder.getUser() == null){
                return MyUtil.getJSONString(999, "未登录");
            }
            User user = userService.getUserByName(toName);
            if(user == null){
                return MyUtil.getJSONString(1, "用户不存在");
            }

            Message message = new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreatedDate(new Date());
//            message.setConversationId(message.getConversationId());
            messageService.addMessage(message);
            return MyUtil.getJSONString(0);
        } catch (Exception e){
            logger.error("发送消息失败："+ e.getMessage());
            return MyUtil.getJSONString(1, "发信失败");
        }
    }

}
