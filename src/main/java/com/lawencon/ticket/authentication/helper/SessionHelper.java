package com.lawencon.ticket.authentication.helper;

import com.lawencon.ticket.authentication.model.UserPrinciple;
import com.lawencon.ticket.persistence.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionHelper {
    public static User getLoginUser() {
        return ((UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUser();
    }

    public static UserPrinciple getUserPrinciple() {
        return (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
