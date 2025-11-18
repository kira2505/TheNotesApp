package com.notesapp.service;

import java.util.Map;

public interface TextService {

    Map<String, Integer> getCountOfUniqWords(String text);
}
