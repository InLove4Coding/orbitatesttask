package com.kuzmin.evgenii.orbitatesttask.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class CountControllerTest {

    @Autowired
    MockMvc mockMvc;

    static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres")
            .withUsername("admin")
            .withPassword("admin")
            .withDatabaseName("test")
            .withExposedPorts(5432);

    static {
        postgreSQLContainer.start();
        System.setProperty("DB_URL", "localhost");
        System.setProperty("DB_PORT", postgreSQLContainer.getMappedPort(5432).toString());
    }

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc.perform(delete("/clearState"));
    }

    @Test
    public void getValue() {
    }

    @Test
    void incrementValue() throws Exception {
        mockMvc.perform(post("/incrementValue"))
                .andExpect(mvcResult -> Assertions.assertEquals("1", mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void clearState() throws Exception {
        //Given
        mockMvc.perform(post("/incrementValue"));

        //When
        mockMvc.perform(delete("/clearState"));

        //Then
        mockMvc.perform(get("/getValue"))
                .andExpect(content().string("0"));
    }

    @Test
    public void testHundredSeqRequest() throws Exception {
        //When
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(post("/incrementValue"));
        }

        //Then
        mockMvc.perform(get("/getValue"))
                .andExpect(content().string("100"));
    }

    @Test
    public void raceConditionTest() throws Exception {
        //When
        for (int i = 0; i < 100; i++) {

            new Thread(() -> {
                try {
                    mockMvc.perform(post("/incrementValue"));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }).start();

        }

        Thread.sleep(10000);

        //Then
        mockMvc.perform(get("/getValue"))
                .andExpect(content().string("100"));
    }
}