package com.shill.gran.common.event.listener;

import com.shill.gran.common.event.UserEvent;
import com.shill.gran.common.framworkEntiry.enums.IdempotentEnum;
import com.shill.gran.common.user.domain.User;
import com.shill.gran.common.user.domain.enums.ItemEnum;
import com.shill.gran.common.user.service.IUserBackpackService;
import com.shill.gran.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserListener {
    @Autowired
    private UserService userDao;
    @Autowired
    private IUserBackpackService iUserBackpackService;
    @TransactionalEventListener(classes = UserEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void userRegisterEvent(UserEvent event){
        User user = event.getUser();
        //送一张改名卡
        iUserBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID, user.getId().toString());
    }
    @EventListener(classes = UserEvent.class)
    public void userTT(){

    }

}
