package org.example.controller;

import org.example.model.FaqItem;
import org.example.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct; // ✅ Исправленный импорт
import java.io.InputStream;
import java.util.Arrays;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostConstruct
    public void loadFaq() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream is = new ClassPathResource("faq.json").getInputStream();
        FaqItem[] items = mapper.readValue(is, FaqItem[].class);
        searchService.loadFaq(Arrays.asList(items));
    }

    @GetMapping("/search")
    public FaqItem search(@RequestParam String query) {
        FaqItem result = searchService.findBestMatch(query);
        if (result == null) {
            FaqItem notFound = new FaqItem();
            notFound.setQuestion(query);
            notFound.setAnswer("К сожалению, я не нашел ответа на этот вопрос.");
            return notFound;
        }
        return result;
    }
}