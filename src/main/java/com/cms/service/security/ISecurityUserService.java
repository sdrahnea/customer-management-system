package com.cms.service.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by sdrahnea
 */
public interface ISecurityUserService extends UserDetailsService {

    User register(User user);
}
