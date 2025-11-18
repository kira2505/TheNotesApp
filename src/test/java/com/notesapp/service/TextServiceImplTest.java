package com.notesapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TextServiceImplTest {

    @InjectMocks
    private TextServiceImpl textService;

    @Test
    void testGetCountOfUniqWords() {
        String text = "Hello, hello world! World... WORLD?";

        Map<String, Integer> result = textService.getCountOfUniqWords(text);

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("world", 3);
        expected.put("hello", 2);

        assertEquals(expected, result);
    }
}