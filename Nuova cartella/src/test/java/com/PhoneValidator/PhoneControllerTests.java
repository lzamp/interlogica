package com.PhoneValidator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import DTO.PhoneNumberExtraction;
import config.PhoneNumberValidator;
import controller.PhoneController;
import entity.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import repository.DataEntryRepository;
import service.PhoneNumberService;

import java.util.Collections;
import java.util.List;

@SpringBootTest (classes = PhoneNumberValidator.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@WebMvcTest(PhoneController.class)
public class PhoneControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhoneNumberService phoneNumberService;

    @MockBean
    private DataEntryRepository dataEntryRepo;

    @Test
    public void testUploadCSVFile_EmptyFile() throws Exception {
        // Simulate an empty file upload
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        mockMvc.perform(multipart("/upload_csv").file("file", file.getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().string("File not uploaded"));
    }

    @Test
    public void testControllAndSavePhoneNumber_Valid() throws Exception {
        String phoneNum = "1234567890";
        List<PhoneNumber> mockResponse = Collections.singletonList(new PhoneNumber());
        when(phoneNumberService.saveNumber(phoneNum)).thenReturn(true);
        when(phoneNumberService.extractElaboratedNumber(phoneNum)).thenReturn(mockResponse);

        mockMvc.perform(get("/controllAndSave/{phoneNum}", phoneNum))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testValidateEndpoint_Success() throws Exception {
        PhoneNumberExtraction extraction = new PhoneNumberExtraction();
        when(phoneNumberService.validatePhoneNumbers()).thenReturn(extraction);

        mockMvc.perform(get("/validate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acceptableNumbers").exists()); // Adjust JSON path based on actual response structure
    }

}
