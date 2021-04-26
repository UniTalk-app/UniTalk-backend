package dev.backend.UniTalk.REST.group;

import dev.backend.UniTalk.group.Group;
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
public class GroupControllerTests
{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupAll() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/all";

        ResponseEntity<String> response = restTemplate.getForEntity(address,String.class );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 12), "{\"_embedded\"");
    }

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupOne() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555";

        ResponseEntity<String> response = restTemplate.getForEntity(address,String.class );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0,15 ), "{\"group_id\":555");
    }

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupNew() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/";

        Group group = new Group("GroupT", 556L,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("idGroup", "556");

        HttpEntity<Group> request = new HttpEntity<>(group, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 11), "{\"group_id\"");
    }

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupNewError() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/";

        Group group = new Group("", 556L,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("idGroup", "556");

        HttpEntity<Group> request = new HttpEntity<>(group, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).contains("Group name: must be between 1 and 128 chars"), true);
    }

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupReplace() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555";

        Group group = new Group("GroupUpdated", 1L,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Group> request = new HttpEntity<>(group, headers);

        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 43), "{\"group_id\":555,\"group_name\":\"GroupUpdated\"");
    }

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupReplaceError() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555";

        Group group = new Group("", 1L,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Group> request = new HttpEntity<>(group, headers);

        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).contains("Group name: must be between 1 and 128 chars"), true);
    }

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupDeleteOne() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Sql(scripts = "/tests/GroupPrepare.sql")
    public void GroupDeleteAll() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}