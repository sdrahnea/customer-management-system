package com.cms.controller.data;

import com.cms.controller.AbstractController;
import com.cms.model.data.LastName;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sdrahnea
 */
@RestController
@RequestMapping(value = "/last-name")
public class LastNameController extends AbstractController<LastName> {
}