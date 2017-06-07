package com.coodeer.wenda;

import com.coodeer.wenda.dao.QuestionDAO;
import com.coodeer.wenda.dao.UserDAO;
import com.coodeer.wenda.model.Question;
import com.coodeer.wenda.model.User;
import com.coodeer.wenda.service.LoggerAspect;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Created by common on 2017/5/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
//@Sql("/init-schema.sql")
public class InitDatabaseTests {

    private static final Logger logger = Logger.getLogger(InitDatabaseTests.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Test
    public void initDatabase(){

//        Random random = new Random();
//        for(int i = 0; i < 1000; i++){
//            User user = new User();
//            user.setName("user_" + i);
//            user.setPassword("");
//            user.setSalt("");
//            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//            userDAO.addUser(user);
//        }
//
//        userDAO.deleteById(1);
//
//        User user = new User();
//        user.setId(2);
//        user.setPassword("123");
//        userDAO.updatePassword(user);
//
//        User selectedUser = userDAO.selectById(2);
//        logger.info(selectedUser.getPassword());

//        for(int i = 0; i < 1000; i++){
//            Question question = new Question();
//            question.setId(i);
//            question.setTitle("title " + i);
//            question.setContent("this is content " + i);
//            Date date = new Date();
//            date.setTime(date.getTime() + i * 60 * 10000);
//            logger.info("dada"+date);
//            question.setCreateDate(date);
//
//            question.setUserId(i+1);
//            questionDAO.addQuetion(question);
//
//        }

//        List<Question> list = questionDAO.selectLatestQuestion(5,4);
//        for(Question q : list)
//            logger.info(q.toString());

        logger.info(userDAO.selectById(2).getHeadUrl());

    }


}
