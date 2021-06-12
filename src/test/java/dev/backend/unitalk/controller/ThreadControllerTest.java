package dev.backend.unitalk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.backend.unitalk.Utils;
import dev.backend.unitalk.group.Group;
import dev.backend.unitalk.payload.request.ThreadRequest;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
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
class ThreadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="user")
    void threadAll() throws Exception {
        mockMvc.perform(get("/api/group/{id}/thread/all", 10))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user")
    void threadOne() throws Exception {
        mockMvc.perform(get("/api/group/{id}/thread/{id}", 10, 10))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("ThreadTitleTest"));
    }

    @Test
    @WithMockUser(username="user")
    void threadNew() throws Exception {
        var token = Utils.InitAuth("testuser", "qwerty", mockMvc);
        mockMvc.perform( post("/api/group/{id}/thread/", 10)
                .content(asJsonString(new ThreadRequest("ThreadTitleNew", -1L)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("ThreadTitleNew"));
    }

    @Test
    @WithMockUser(username="user")
    void threadReplace() throws Exception {

        Group g = new Group("GroupTitle", 10L, new Timestamp(System.currentTimeMillis()));
        var token = Utils.InitAuth("testuser", "qwerty", mockMvc);

        mockMvc.perform( put("/api/group/{id}/thread/{id}", 10, 10)
                .content(asJsonString(new ThreadRequest("ThreadTitleReplace", 11L)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("ThreadTitleReplace"));
    }

    @Test
    void threadDeleteOneAsCreator() throws Exception {
        var token = Utils.InitAuth("testuser", "qwerty", mockMvc);
        var result = mockMvc.perform(delete("/api/group/{id}/thread/{id}", 10, 10)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        Assert.isTrue(result.getResponse().getContentAsString().contains("Thread successfully deleted"));
    }

    @Test
    void threadDeleteOneAsModerator() throws Exception {
        var token = Utils.InitAuth("moderator", "qwerty", mockMvc);
        var result = mockMvc.perform(delete("/api/group/{id}/thread/{id}", 11, 11)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        Assert.isTrue(result.getResponse().getContentAsString().contains("Thread successfully deleted"));
    }

    @Test
    void threadDeleteOneAsAdmin() throws Exception {
        var token = Utils.InitAuth("admin", "qwerty", mockMvc);
        var result = mockMvc.perform(delete("/api/group/{id}/thread/{id}", 11, 11)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        Assert.isTrue(result.getResponse().getContentAsString().contains("Thread successfully deleted"));
    }

    @Test
    void threadDeleteOneAsUnauthorized() throws Exception {
        var token = Utils.InitAuth("testuser", "qwerty", mockMvc);
        var result = mockMvc.perform(delete("/api/group/{id}/thread/{id}", 11, 11)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(405))
                .andReturn();

        Assert.isTrue(result.getResponse().getContentAsString().contains("No rights to delete this resource"));
    }

    @Test
    @WithMockUser(username="user")
    void threadDeleteAll() throws Exception {
        mockMvc.perform( delete("/api/group/{id}/thread/", 10) )
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