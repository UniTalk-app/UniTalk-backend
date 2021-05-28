package dev.backend.unitalk;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class Utils {
    public static String InitAuth(String username, String password, MockMvc mockMvc) throws Exception {
        MvcResult result = mockMvc.perform( post("/api/auth/login")
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        var token = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(token);
        return jsonObject.getString("token");
    }
}
