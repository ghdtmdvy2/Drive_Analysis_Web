package com.example.driveanalysis.controller;

import com.example.driveanalysis.answer.controller.AnswerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithUserDetails("user1")
class AnswerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_article_list() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(post("/answer/create/2")
                        .with(csrf())
                        .param("content", "댓글 내용1")
                )
                .andDo(print());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("detail"))
                .andExpect(redirectedUrlPattern("/article/2#*"));
    }

    @Test
    void get_modify_answer() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get("/answer/modify/2"))
                .andDo(print());
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("answerModify"));
    }

    @Test
    void post_modify_answer() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(post("/answer/modify/2")
                        .with(csrf())
                        .param("content", "댓글 수정 내용1")
                )
                .andDo(print());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("answerModify"))
                .andExpect(redirectedUrlPattern("/article/2#*"));
    }

    @Test
    void get_delete_answer() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get("/answer/delete/3"))
                .andDo(print());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("answerDelete"))
                .andExpect(redirectedUrl("/article/2"));
    }
}