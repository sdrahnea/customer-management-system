package com.cms.controllers;

import com.cms.model.security.SecurityUser;
import com.cms.services.impl.SecurityUserService;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sdrahnea
 */
@RestController
public class MainRestController {

    private final SecurityUserService userService;

    public MainRestController(SecurityUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/register")
    public SecurityUser register(@RequestBody SecurityUser user){
        return userService.register(user);
    }

}
