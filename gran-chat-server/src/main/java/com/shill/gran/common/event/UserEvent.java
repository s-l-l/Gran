package com.shill.gran.common.event;

import com.shill.gran.common.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {
    private final User user;

    public UserEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
