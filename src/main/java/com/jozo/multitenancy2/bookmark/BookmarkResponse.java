package com.jozo.multitenancy2.bookmark;

import java.time.LocalDateTime;

public record BookmarkResponse(
    Long id,
    String url,
    String title,
    String description,
    LocalDateTime created,
    Long userId,
    String userEmail
) {}
