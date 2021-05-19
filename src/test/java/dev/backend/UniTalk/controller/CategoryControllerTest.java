package dev.backend.UniTalk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.backend.UniTalk.category.Category;
import dev.backend.UniTalk.group.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/schema.sql")
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    @WithMockUser(username="user")
    void categoryAll() throws Exception {
        mockMvc.perform(get("/api/group/{id}/category/all", 10))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user")
    void categoryOne() throws Exception {
        mockMvc.perform(get("/api/group/{id}/category/{id}", 10, 10))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("CategoryTitleTest"));
    }

    @Test
    @WithMockUser(username="user")
    void categoryNew() throws Exception {

        Group g = new Group("GroupTitle", 10L, new Timestamp(System.currentTimeMillis()));

        mockMvc.perform( post("/api/group/{id}/category/", 10)
                .content(asJsonString(new Category("CategoryTitleNew", g, new Timestamp(System.currentTimeMillis()))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("CategoryTitleNew"));
    }

    @Test
    @WithMockUser(username="user")
    void categoryReplace() throws Exception {

        Group g = new Group("GroupTitle", 10L, new Timestamp(System.currentTimeMillis()));

        mockMvc.perform( put("/api/group/{id}/category/{id}", 10, 10)
                .content(asJsonString(new Category("CategoryTitleReplace", g, new Timestamp(System.currentTimeMillis()))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("CategoryTitleReplace"));
    }


    @Test
    @WithMockUser(username="user")
    void categoryDeleteOne() throws Exception {
        mockMvc.perform( delete("/api/group/{id}/category/{id}", 10, 10) )
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser(username="user")
    void categoryDeleteAll() throws Exception {
        mockMvc.perform( delete("/api/group/{id}/category/", 10) )
                .andExpect(status().is(204));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}