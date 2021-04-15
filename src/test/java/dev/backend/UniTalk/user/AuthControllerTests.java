package dev.backend.UniTalk.user;

import dev.backend.UniTalk.payload.request.LoginRequest;
import dev.backend.UniTalk.payload.request.RegisterRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void RegisterWithValidRequest() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("user");
        registerRequest.setEmail("test@a.com");
        registerRequest.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> request = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("{\"message\":\"User registered successfully!\"}", response.getBody());
    }

    @Test
    public void RegisterWithInvalidRequest_UsernameTaken() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setEmail("test@a.com");
        registerRequest.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> request = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("{\"message\":\"Error: Username is already taken!\"}", response.getBody());
    }

    @Test
    public void RegisterWithInvalidRequest_EmailTaken() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("user2");
        registerRequest.setEmail("email");
        registerRequest.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> request = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("{\"message\":\"Error: Email is already in use!\"}", response.getBody());
    }

    @Test
    public void LoginWithValidRequest() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/login";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("qwerty");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody().contains("\"type\":\"Bearer\",\"id\":1,\"username\":\"username\",\"email\":\"email\",\"roles\":"));
    }
}
