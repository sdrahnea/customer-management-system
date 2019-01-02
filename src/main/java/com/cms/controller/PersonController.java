package com.cms.controller;

import com.cms.model.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sdrahnea
 */
@RestController
@RequestMapping(value = "/person")
public class PersonController extends AbstractController<Person> {
}