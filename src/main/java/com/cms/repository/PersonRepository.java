package com.cms.repository;

import com.cms.model.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sdrahnea
 */
public interface PersonRepository extends CrudRepository<Person, Long> {

}