package dev.backend.UniTalk.REST.category;

import dev.backend.UniTalk.category.Category;

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
public class CategoryControllerTests
{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryAll() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/all";

        ResponseEntity<String> response = restTemplate.getForEntity(address,String.class );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 12), "{\"_embedded\"");
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryOne() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/2";

        ResponseEntity<String> response = restTemplate.getForEntity(address,String.class );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0,7 ), "{\"id\":2");
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryNew() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category";

        Category category=new Category("Cat3", null,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("idGroup", "555");

        HttpEntity<Category> request = new HttpEntity<>(category, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 21), "{\"id\":3,\"name\":\"Cat3\"");
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryReplace() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/1";

        Category category=new Category("CatNew", null,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Category> request = new HttpEntity<>(category, headers);

        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 23), "{\"id\":1,\"name\":\"CatNew\"");
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryDelete() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/1";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
