package com.devam.urlshortner.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("short_urls")
public class ShortUrl {

    @Id
    private String id;

    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiryAt;
    private long clickCount;
}
