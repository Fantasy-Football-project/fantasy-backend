package com.sathvik.components;

import com.sathvik.services.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DraftScheduler {

    private final DraftService draftService;

    @Scheduled(cron = "0 * * * * *")
    public void draftStartCheck() {
        System.out.println("Draft Check started");
        draftService.checkDraftStarts();
    }
}
