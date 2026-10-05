package com.codingshuttle.projects.lovable_clone.dto.subscription;

public record UsageTodayResponse(
         Integer previewsRunning,
         Integer tokensLimit,
         Integer tokensUsed,
         Integer previewsLimit
) {
}
