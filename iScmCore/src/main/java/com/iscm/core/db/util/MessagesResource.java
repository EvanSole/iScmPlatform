package com.iscm.core.db.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Locale;

public class MessagesResource implements Messages , ApplicationListener<ApplicationEvent> {

    protected ApplicationContext context = null;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent) {
            context = ((ContextRefreshedEvent)event).getApplicationContext();
            return;
        }
        if(event instanceof ContextClosedEvent) {
            context = null;
        }
    }

    @Override
    public String getMessage(String code) {
        assert(context != null);
        return context.getMessage(code, null, Locale.SIMPLIFIED_CHINESE);
    }

    @Override
    public String getMessage(String code, Object... args) {
        assert(context != null);
        return context.getMessage(code, args, Locale.getDefault());
    }

}
