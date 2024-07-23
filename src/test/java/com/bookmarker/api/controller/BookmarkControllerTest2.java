package com.bookmarker.api.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
   "spring.datasource.url=jdbc:tc:postgresql:latest:///bookmarkTestdb",
   "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
class BookmarkControllerTest2 {
    @Autowired
    private MockMvc mvc;
    
   @Test
    void shouldCreateBookmarkSuccessfully() throws Exception {
        this.mvc.perform(
            post("/api/bookmarks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
            {
                "title": "SpringBoot Blog",
                "url": "https://SpringBoot.in"
            }
            """)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.title", is("SpringBoot Blog")))
        .andExpect(jsonPath("$.url", is("https://SpringBoot.in")));
    }

    @Test
    void shouldFailToCreateBookmarkWhenUrlIsNotPresent() throws Exception {
        this.mvc.perform(
                post("/api/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "title": "SpringBoot Blog"
                }
                """)
            )
            .andExpect(status().isBadRequest())
            .andExpect(header().string("Content-Type", is("application/problem+json")))
            .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
            .andExpect(jsonPath("$.title", is("Constraint Violation")))
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.violations", hasSize(1)))
            .andExpect(jsonPath("$.violations[0].field", is("url")))
            .andExpect(jsonPath("$.violations[0].message", is("Url should not be empty")))
            .andReturn();
    }
}   