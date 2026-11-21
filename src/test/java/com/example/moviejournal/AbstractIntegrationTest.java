package com.example.moviejournal;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base class for integration tests: the full application (security, controllers, SQL)
 * runs against a real PostgreSQL; schema.sql is applied because spring.sql.init.mode=always.
 * <p>
 * Tests don't clean the database between runs. Instead, every test registers its own
 * users with unique names, so tests can't see each other's data.
 */
@SpringBootTest(properties = "jwt.secret=test-secret-that-is-at-least-32-characters-long")
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public abstract class AbstractIntegrationTest {

    protected static final String PASSWORD = "password123";

    @Autowired
    protected MockMvc mockMvc;

    protected static String uniqueUsername() {
        return "user-" + UUID.randomUUID().toString().substring(0, 8);
    }

    protected void register(String username, String password) throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsJson(username, password)))
                .andExpect(status().isCreated());
    }

    protected String login(String username, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsJson(username, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.token");
    }

    /** Registers a brand-new user and returns a valid JWT for it. */
    protected String newUserToken() throws Exception {
        String username = uniqueUsername();
        register(username, PASSWORD);
        return login(username, PASSWORD);
    }

    protected static String bearer(String token) {
        return "Bearer " + token;
    }

    protected static String credentialsJson(String username, String password) {
        return """
                {"username": "%s", "password": "%s"}
                """.formatted(username, password);
    }
}
