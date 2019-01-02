package com.cms.controller;

import com.cms.model.UnitType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sdrahnea
 */
@RestController
@RequestMapping(value = "/unit-type")
public class UnitTypeController extends AbstractController<UnitType> {
}