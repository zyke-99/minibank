package com.zyke.minibank.auditing;

import com.zyke.minibank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MinibankRevisionListener implements RevisionListener {

    private final UserService userService;

    @Override
    public void newRevision(Object revisionEntity) {

        MinibankRevisionEntity minibankRevision = (MinibankRevisionEntity) revisionEntity;
        userService.getCurrentUser().ifPresent(minibankRevision::setRevisedBy);
    }
}
