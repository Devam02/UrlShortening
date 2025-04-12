package com.devam.urlshortner.service;

import com.devam.urlshortner.model.ShortUrl;
import com.devam.urlshortner.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository repository;
    private final StringRedisTemplate redisTemplate;

    public String shortenUrl(String originalUrl, int expiryMinutes) {
        String shortCode = UUID.randomUUID().toString().substring(0, 8);

        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiryAt(LocalDateTime.now().plusMinutes(expiryMinutes));
        url.setClickCount(0);

        repository.save(url);
        redisTemplate.opsForValue().set(shortCode, originalUrl, Duration.ofMinutes(expiryMinutes));

        return shortCode;
    }

    

    public Optional<String> getOriginalUrl(String shortCode) {
        String cached = redisTemplate.opsForValue().get(shortCode);
        if (cached != null) return Optional.of(cached);

        Optional<ShortUrl> optional = repository.findByShortCode(shortCode);
        optional.ifPresent(url -> {
            redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl(), Duration.between(LocalDateTime.now(), url.getExpiryAt()));
            url.setClickCount(url.getClickCount() + 1);
            repository.save(url);
        });

        return optional.map(ShortUrl::getOriginalUrl);
    }
    
    public Optional<ShortUrl> getUrlDetails(String shortCode) {
    return repository.findByShortCode(shortCode);
}


}
