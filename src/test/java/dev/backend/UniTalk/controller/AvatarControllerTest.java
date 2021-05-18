package dev.backend.UniTalk.controller;


import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/schema.sql")
@SpringBootTest
@AutoConfigureMockMvc
public class AvatarControllerTest {

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
}
