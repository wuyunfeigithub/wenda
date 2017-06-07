package com.coodeer.wenda.service;

import com.coodeer.wenda.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by common on 2017/5/14.
 */
@Service
public class MyService {

    public String doSomeThing(){
        System.out.println("In service do thing");
        return "I already do some things";
    }
}
