package dev.backend.UniTalk.REST.thread;

import dev.backend.UniTalk.thread.Thread;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThreadControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadAll() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread/all";

        ResponseEntity<String> response = restTemplate.getForEntity(address,String.class );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 12), "{\"_embedded\"");
    }

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadOne() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread/444";

        ResponseEntity<String> response = restTemplate.getForEntity(address,String.class );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 12), "{\"thread_id\"");
    }

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadNew() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread";

        Thread thread = new Thread("ThreadTitle", 1L, 44L, null,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("idGroup", "555");

        HttpEntity<Thread> request = new HttpEntity<>(thread, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 12), "{\"thread_id\"");
    }

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadNewError() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread";

        Thread thread = new Thread("", 1L, 44L, null,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("idGroup", "555");

        HttpEntity<Thread> request = new HttpEntity<>(thread, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).contains("Thread name: must be between 1 and 128 chars"), true);
    }

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadReplace() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread/444";

        Thread thread = new Thread("ThreadTitle", 1L, 44L, null,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Thread> request = new HttpEntity<>(thread, headers);

        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 12), "{\"thread_id\"");
    }

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadReplaceError() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread/444";

        Thread thread = new Thread("", 1L, 44L, null,
                1L, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Thread> request = new HttpEntity<>(thread, headers);

        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).contains("Thread name: must be between 1 and 128 chars"), true);
    }

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadDeleteOne() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread/444";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Sql(scripts = "/tests/ThreadPrepare.sql")
    public void ThreadDeleteAll() throws Exception {

        final String address = "http://localhost:" + port + "/api/group/555/thread/";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
