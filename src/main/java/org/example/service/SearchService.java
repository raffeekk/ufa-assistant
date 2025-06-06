package org.example.service;

import org.example.model.FaqItem;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private List<FaqItem> faqList = new ArrayList<>();
    private Map<String, Set<Integer>> wordIndex = new ConcurrentHashMap<>();
    private Map<String, FaqItem> exactMatchCache = new ConcurrentHashMap<>();

    public void loadFaq(List<FaqItem> items) {
        faqList.clear();
        wordIndex.clear();
        exactMatchCache.clear();
        
        for (int i = 0; i < items.size(); i++) {
            FaqItem item = items.get(i);
            faqList.add(item);
            
            // Индексация слов
            String[] words = normalize(item.getQuestion()).split("\\s+");
            for (String word : words) {
                wordIndex.computeIfAbsent(word, k -> new HashSet<>()).add(i);
            }
            
            // Кэширование точных совпадений
            exactMatchCache.put(normalize(item.getQuestion()), item);
        }
        
        logger.info("Загружено {} вопросов, проиндексировано {} уникальных слов", 
            items.size(), wordIndex.size());
    }

    private String normalize(String text) {
        return text.toLowerCase()
                .replaceAll("[^а-яёa-z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    @Cacheable(value = "searchResults", key = "#query")
    public FaqItem findBestMatch(String query) {
        logger.info("Поиск по запросу: {}", query);
        if (faqList.isEmpty()) {
            logger.warn("Список FAQ пуст");
            return null;
        }

        String normalizedQuery = normalize(query);
        
        // Проверка точного совпадения в кэше
        FaqItem exactMatch = exactMatchCache.get(normalizedQuery);
        if (exactMatch != null) {
            logger.info("Найдено точное совпадение в кэше");
            return exactMatch;
        }

        String[] words = normalizedQuery.split("\\s+");
        Map<FaqItem, Double> scores = new HashMap<>();

        // Используем индекс для быстрого поиска
        Set<Integer> matchingIndices = new HashSet<>();
        for (String word : words) {
            Set<Integer> indices = wordIndex.get(word);
            if (indices != null) {
                matchingIndices.addAll(indices);
            }
        }

        // Вычисляем релевантность только для найденных индексов
        for (Integer index : matchingIndices) {
            FaqItem item = faqList.get(index);
            double score = calculateRelevance(normalizedQuery, normalize(item.getQuestion()));
            scores.put(item, score);
        }

        FaqItem result = scores.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (result != null) {
            logger.info("Найден ответ с релевантностью {}", scores.get(result));
        } else {
            logger.info("Ответ не найден");
        }

        return result;
    }

    private double calculateRelevance(String query, String question) {
        if (query.equals(question)) {
            return 1.0;
        }

        String[] queryWords = query.split("\\s+");
        String[] questionWords = question.split("\\s+");
        
        int matches = 0;
        for (String qWord : queryWords) {
            for (String questionWord : questionWords) {
                if (questionWord.contains(qWord) || qWord.contains(questionWord)) {
                    matches++;
                }
            }
        }
        
        return (double) matches / (queryWords.length + questionWords.length);
    }

    public List<String> getCategories() {
        return faqList.stream()
                .map(FaqItem::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<FaqItem> getAllFaqItems() {
        return new ArrayList<>(faqList);
    }
}