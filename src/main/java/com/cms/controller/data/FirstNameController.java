package com.cms.controller.data;

import com.cms.controller.AbstractController;
import com.cms.model.data.FirstName;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sdrahnea
 */
@RestController
@RequestMapping(value = "/first-name")
public class FirstNameController extends AbstractController<FirstName> {
}