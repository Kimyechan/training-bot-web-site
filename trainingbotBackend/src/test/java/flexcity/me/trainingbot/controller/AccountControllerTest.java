package flexcity.me.trainingbot.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class AccountControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Test
//    public void processingSignUp() throws Exception {
//        mockMvc.perform(post("/api/signUp")
//                .param("userId", "rladPcks7" )
//                .param("password", "1234")
//                .param("name", "kimyechan")
//                .param("phoneNumber", "01092551974")
//                .with(csrf()))
//                .andDo(print());
//    }
//}