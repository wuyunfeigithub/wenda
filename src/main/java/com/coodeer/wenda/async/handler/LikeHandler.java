package com.coodeer.wenda.async.handler;

import com.coodeer.wenda.async.EventHandler;
import com.coodeer.wenda.async.EventModel;
import com.coodeer.wenda.async.EventType;
import com.coodeer.wenda.model.Message;
import com.coodeer.wenda.model.User;
import com.coodeer.wenda.service.MessageService;
import com.coodeer.wenda.service.UserService;
import com.coodeer.wenda.utils.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by common on 2017/6/16.
 */
@Component
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();

        message.setFromId(MyUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的评论，链接：" + MyUtil.mySiteAdress + "/question/" + model.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
