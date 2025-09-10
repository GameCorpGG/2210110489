package com.example.url_shortener.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UrlService {

    // Store shortened URL data in-memory
    private final Map<String, UrlData> urlStore = new HashMap<>();

    public String shortenUrl(String originalUrl, Integer validity, String shortcode) {
        String code = (shortcode != null && !shortcode.isBlank()) ? shortcode : UUID.randomUUID().toString().substring(0, 6);

        // Expiry time
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(validity != null ? validity : 30);

        urlStore.put(code, new UrlData(originalUrl, expiry));
        return code;
    }

    public String getOriginalUrl(String shortId) {
        UrlData data = urlStore.get(shortId);
        if (data == null || LocalDateTime.now().isAfter(data.expiry)) {
            return null; // expired or not found
        }
        return data.originalUrl;
    }

    // Simple inner class instead of entity/DTO
    static class UrlData {
        String originalUrl;
        LocalDateTime expiry;

        UrlData(String originalUrl, LocalDateTime expiry) {
            this.originalUrl = originalUrl;
            this.expiry = expiry;
        }
    }
}
