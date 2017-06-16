package com.coodeer.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.coodeer.wenda.utils.JedisAdapter;
import com.coodeer.wenda.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by common on 2017/6/16.
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);

            return true;
        }catch (Exception e){
            return false;
        }
    }

}
