package com.devam.urlshortner.controller;

import java.util.Optional;
import com.devam.urlshortner.model.ShortUrl;

import com.devam.urlshortner.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/shorten")
    public ResponseEntity<?> shorten(@RequestBody Map<String, Object> body) {
        String originalUrl = (String) body.get("url");
        int expiry = (Integer) body.getOrDefault("expiry", 60);
        String shortCode = urlService.shortenUrl(originalUrl, expiry);

        return ResponseEntity.ok(Map.of("shortUrl", "http://localhost:8080/" + shortCode));
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        var url = urlService.getOriginalUrl(shortCode);
        if (url.isPresent()) {
            response.sendRedirect(url.get());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/api/info/{shortCode}")
    public ResponseEntity<?> getInfo(@PathVariable String shortCode) {
        Optional<ShortUrl> info = urlService.getUrlDetails(shortCode);

        return info.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
