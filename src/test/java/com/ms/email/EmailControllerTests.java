package com.ms.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.email.dtos.EmailDto;
import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendingValidEmail() throws Exception {
        EmailDto emailDto = new EmailDto();
        emailDto.setOwnerRef("Owner");
        emailDto.setEmailFrom("valid@example.com");
        emailDto.setEmailTo("recipient@example.com");
        emailDto.setSubject("Test Subject");
        emailDto.setText("Test Email Content");

        EmailModel emailModel = new EmailModel();
        emailModel.setStatusEmail(StatusEmail.SENT);

        Mockito.when(emailService.sendEmail(Mockito.any(EmailModel.class))).thenReturn(emailModel);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/send-single")
                        .content(asJsonString(emailDto))
                        .contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void testSendingInvalidEmail() throws Exception {
        EmailDto emailDto = new EmailDto();
        emailDto.setOwnerRef("");
        emailDto.setEmailFrom("invalid-email");
        emailDto.setEmailTo("");
        emailDto.setSubject("");
        emailDto.setText("");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/send-single")
                        .content(asJsonString(emailDto))
                        .contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    // Utility method to convert Java object into JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
