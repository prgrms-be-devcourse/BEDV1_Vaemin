package com.programmers.devcourse.vaemin.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.devcourse.vaemin.shop.dto.CategoryDto;
import com.programmers.devcourse.vaemin.shop.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    ObjectMapper objectMapper;

    CategoryDto categoryDto;
    Long categoryId;

    @BeforeEach
    void init() {
        categoryDto = new CategoryDto("fastfood");
        categoryId = categoryService.createCategory(categoryDto);
    }

    @Test
    void createCategory() throws Exception {
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryDto("category"))))
                .andExpect(status().isOk()) // 200
                .andDo(print());
    }

    @Test
    void deleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateCategory() throws Exception {
        mockMvc.perform(put("/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryDto("update Category"))))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
