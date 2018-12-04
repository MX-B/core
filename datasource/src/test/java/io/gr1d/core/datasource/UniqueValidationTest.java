package io.gr1d.core.datasource;

import io.gr1d.core.Gr1dCoreApplication;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringTestApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class })
public class UniqueValidationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private Flyway flyway;

    @After
    public void tearDown() {
        flyway.clean();
    }

    @Test
    public void testUniqueValidator() throws Exception {
        mockMvc.perform(post("/mock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"uniqueProperty\": \"123456789\" }"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/mock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"uniqueProperty\": \"123456789\" }"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(delete("/mock"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/mock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"uniqueProperty\": \"123456789\" }"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}


