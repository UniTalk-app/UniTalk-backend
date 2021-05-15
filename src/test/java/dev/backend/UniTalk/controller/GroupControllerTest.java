package dev.backend.UniTalk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="user")
    void groupOne() throws Exception {
        mockMvc.perform(get("/api/group/{id}", 10))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupName").value("GroupTitleTest"));
    }

    @Test
    @WithMockUser(username="user")
    void groupNew() throws Exception {
        mockMvc.perform( post("/api/group/")
                .content(asJsonString(new Group("GroupTitleTestNew", 10L, new Timestamp(System.currentTimeMillis()))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupName").value("GroupTitleTestNew"));
    }

    @Test
    @WithMockUser(username="user")
    void groupReplace() throws Exception {
        mockMvc.perform( put("/api/group/{id}", 10)
                .content(asJsonString(new Group("GroupTitleTestReplace", 10L, new Timestamp(System.currentTimeMillis()))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupName").value("GroupTitleTestReplace"));
    }

    @Test
    @WithMockUser(username="user")
    void groupDeleteOne() throws Exception {
        mockMvc.perform( delete("/api/group/{id}", 10) )
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser(username="user")
    void groupDeleteAll() throws Exception {
        mockMvc.perform( delete("/api/group/") )
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