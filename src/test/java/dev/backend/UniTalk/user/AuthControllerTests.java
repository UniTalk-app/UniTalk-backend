package dev.backend.UniTalk.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithValidRequest() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("user");
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("test@a.com");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        assertEquals(1, userRepository.count());

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("{\"message\":\"User registered successfully!\"}", response.getBody());
        assertEquals(2, userRepository.count());
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_NoUsername() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("test@a.com");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("null value in column username"));
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_NoEmail() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("user");
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("null value in column email"));
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_NoPassword() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("user");
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("test@a.com");
        user.setPassword("");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("password: must be between 1 and 128 chars"));
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_NoFirstName() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("user");
        user.setLastName("last");
        user.setEmail("test@a.com");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("null value in column first_name"));
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_NoLastName() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("user");
        user.setFirstName("first");
        user.setEmail("test@a.com");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("null value in column last_name"));
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_UsernameTaken() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("rafal");
        user.setFirstName("f");
        user.setLastName("l");
        user.setEmail("test@a.com");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("Error: Username is already in use!\""));
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_EmailTaken() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("user2");
        user.setFirstName("f");
        user.setLastName("l");
        user.setEmail("email@email");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("Error: Email is already in use!\""));
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void LoginWithValidRequest() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/login";
        User user = new User();
        user.setUsername("rafal");
        user.setPassword("qwerty");
        user.setEmail("email@email");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(true, Objects.requireNonNull(response.getBody()).contains("\"username\":\"rafal\",\"email\":\"email@email\""));
    }
}
