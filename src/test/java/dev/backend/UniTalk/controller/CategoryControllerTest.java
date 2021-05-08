package dev.backend.UniTalk.controller;

import dev.backend.UniTalk.category.Category;
import dev.backend.UniTalk.category.CategoryController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="user")
    void categoryAll() throws Exception {
        mockMvc.perform(get("/api/group/1/category/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user")
    void categoryOne() throws Exception {

    }

    @Test
    @WithMockUser(username="user")
    void categoryNew() throws Exception {

    }

    @Test
    @WithMockUser(username="user")
    void categoryReplace() throws Exception {

    }


    @Test
    @WithMockUser(username="user")
    void categoryDeleteOne() throws Exception {

    }

    @Test
    @WithMockUser(username="user")
    void categoryDeleteAll() throws Exception {

    }
}