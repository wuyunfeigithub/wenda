package com.coodeer.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.coodeer.wenda.utils.JedisAdapter;
import com.coodeer.wenda.utils.RedisKeyUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by common on 2017/6/16.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{

    private static final Logger logger = Logger.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventType();

                for(EventType type : eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);

                    for (String message : events){
                        if(message.equals(key))
                            continue;

                        EventModel eventModel = JSONObject.parseObject(message, EventModel.class);
                        if(!config.containsKey(eventModel.getEventType())){
                            logger.error("不能识别的事件类型");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getEventType())){
                            handler.doHandle(eventModel);
                        }
                    }

                }
            }
        });
        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
