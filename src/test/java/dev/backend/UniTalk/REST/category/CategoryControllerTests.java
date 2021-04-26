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
        final String address = "http://localhost:" + port + "/api/group/555/category/44";

        ResponseEntity<String> response = restTemplate.getForEntity(address,String.class );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0,17 ), "{\"category_id\":44");
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
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 14), "{\"category_id\"");
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryNewError() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category";

        Category category=new Category("", null,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("idGroup", "555");

        HttpEntity<Category> request = new HttpEntity<>(category, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(address, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).contains("Category name: must be between 1 and 128 chars"), true);
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryReplace() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/55";

        Category category=new Category("CatUpdated", null,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Category> request = new HttpEntity<>(category, headers);

        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).substring(0, 37), "{\"category_id\":55,\"name\":\"CatUpdated\"");
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryReplaceError() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/55";

        Category category = new Category("", null,new Timestamp(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Category> request = new HttpEntity<>(category, headers);

        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).contains("Category name: must be between 1 and 128 chars"), true);
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryDeleteOne() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/55";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Sql(scripts = "/tests/CategoryPrepare.sql")
    public void CategoryDeleteAll() throws Exception
    {
        final String address = "http://localhost:" + port + "/api/group/555/category/";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(address, HttpMethod.DELETE, request, String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
