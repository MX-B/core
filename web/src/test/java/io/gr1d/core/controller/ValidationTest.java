package io.gr1d.core.controller;

import io.gr1d.core.Gr1dCoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Gr1dCoreApplication.class)
public class ValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testErrorMessages() throws Exception {
        mockMvc.perform(post("/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"\", \"documentNumber\": \"12345678919\" }"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].error").value("ObjectError"))
//                .andExpect(jsonPath("$[1].message").value("Este campo precisa ser preenchido"))
//                .andExpect(jsonPath("$[1].meta").value("name"))
                .andExpect(jsonPath("$[0].timestamp").isNotEmpty())
                .andExpect(jsonPath("$[1].error").value("ObjectError"))
//                .andExpect(jsonPath("$[0].message").value("CPF ou CNPJ inválido"))
//                .andExpect(jsonPath("$[0].meta").value("document_number"))
                .andExpect(jsonPath("$[1].timestamp").isNotEmpty());
    }

    @Test
    public void testValidationLocalDate() throws Exception {
        mockMvc.perform(get("/validate?date_start=2018-12-01"))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/validate"))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/validate?date_start=abcd-12-01"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].error").value("ObjectError"))
                .andExpect(jsonPath("$[0].message").value("Invalid format"))
                .andExpect(jsonPath("$[0].meta").value("date_start"));
    }
}


