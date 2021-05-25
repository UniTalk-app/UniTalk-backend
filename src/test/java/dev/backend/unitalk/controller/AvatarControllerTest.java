package dev.backend.unitalk.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/schema.sql")
@SpringBootTest
@AutoConfigureMockMvc
class AvatarControllerTest {

    private String token;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() throws Exception {
        MvcResult result = mockMvc.perform( post("/api/auth/login")
                .content("{\"username\":\"testuser\",\"password\":\"qwerty\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        token = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(token);
        token = jsonObject.getString("token");
    }

    @Test
    void getAvatar() throws Exception {
        mockMvc.perform(get("/api/avatar")
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addAvatar() throws Exception {

        //===============================================
        // login another user
        //===============================================

        MvcResult result = mockMvc.perform( post("/api/auth/login")
                .content("{\"username\":\"testuser2\",\"password\":\"qwerty\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        token = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(token);
        token = jsonObject.getString("token");

        //===============================================
        // test post request
        //===============================================

        // simulate image file
        MockMultipartFile image = new MockMultipartFile("image", "", "application/json", "{\"image\": \"aabbcc\"}".getBytes());

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/api/avatar")
                        .file(image)
                        .characterEncoding("UTF-8")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateAvatar() throws Exception {

        // simulate image file
        MockMultipartFile image = new MockMultipartFile("image", "", "application/json", "{\"image\": \"aabbccnew\"}".getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/avatar");
        builder.with(request -> { request.setMethod("PUT"); return request; });

        mockMvc.perform(builder
                        .file(image)
                        .characterEncoding("UTF-8")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteAvatar() throws Exception {

        mockMvc.perform( delete("/api/avatar")
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + token))
                .andExpect(status().is(204));

    }
}
