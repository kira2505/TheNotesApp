package com.notesapp.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TextServiceImpl implements TextService {

    @Override
    public Map<String, Integer> getCountOfUniqWords(String text) {
        Map<String, Integer> wordsCount = new HashMap<>();

        String[] words = text.toLowerCase()
                .replaceAll("[^\\p{L}\\d\\s]", "").split("\\s+");

        for (String word : words) {
            if (!word.isBlank()) {
                wordsCount.put(word, wordsCount.getOrDefault(word, 0) + 1);
            }
        }

        return wordsCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll);
    }
}
