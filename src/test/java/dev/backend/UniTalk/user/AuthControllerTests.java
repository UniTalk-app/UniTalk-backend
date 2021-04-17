package dev.backend.UniTalk.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

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

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Error: User data is not complete!\"}", response.getBody());
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

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Error: User data is not complete!\"}", response.getBody());
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Error: User data is not complete!\"}", response.getBody());
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

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Error: User data is not complete!\"}", response.getBody());
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

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Error: User data is not complete!\"}", response.getBody());
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

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("{\"message\":\"Error: Username is already taken!\"}", response.getBody());
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void RegisterWithInvalidRequest_EmailTaken() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/register";
        User user = new User();
        user.setUsername("user2");
        user.setFirstName("f");
        user.setLastName("l");
        user.setEmail("email");
        user.setPassword("ha");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("{\"message\":\"Error: Email is already in use!\"}", response.getBody());
    }

    @Test
    @Sql(scripts = "/tests/UserPrepare.sql")
    public void LoginWithValidRequest() throws Exception {
        final String address = "http://localhost:" + port + "/api/auth/login";
        User user = new User();
        user.setUsername("rafal");
        user.setPassword("qwerty");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody().contains("\"type\":\"Bearer\",\"id\":1,\"username\":\"rafal\",\"email\":\"email\",\"roles\":"));
    }
}
