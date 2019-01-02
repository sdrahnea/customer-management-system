package com.cms.contextHolder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.faces.bean.ManagedBean;

/**
 * Created by sdrahnea
 */
@ManagedBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloWorld {

    private String firstName="Ion";
    private String lastName="Cojocari";

    public String showGreeting(){
        return "Hi "+firstName+" "+lastName;
    }
}
