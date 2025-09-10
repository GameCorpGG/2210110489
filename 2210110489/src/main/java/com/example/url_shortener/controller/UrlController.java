package com.example.url_shortener.controller;

import com.example.url_shortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shorturls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, Object> body) {
        String originalUrl = (String) body.get("url");
        String shortcode = (String) body.get("shortcode");

        // Safe parsing of validity
        Object validityObj = body.get("validity");
        Integer validity = null;
        if (validityObj != null) {
            validity = Integer.parseInt(validityObj.toString());
        }

        String code = urlService.shortenUrl(originalUrl, validity, shortcode);

        return ResponseEntity.status(201).body(Map.of(
                "shortLink", "http://localhost:8080/shorturls/" + code,
                "expiry", "valid"
        ));
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<?> redirect(@PathVariable String shortId) {
        String originalUrl = urlService.getOriginalUrl(shortId);
        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("originalUrl", originalUrl));
    }
}

