package com.coodeer.wenda.async;

import java.util.List;

/**
 * Created by common on 2017/6/16.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventType();
}
