package com.sathvik.components;

import com.sathvik.services.DraftService;
import com.sathvik.services.UpdateNFLPlayersService;
import com.sathvik.services.UpdatePlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePlayersScheduler {
    private final UpdateNFLPlayersService updateNFLPlayersService;

    //@Scheduled(cron = "0 * * * * *")
    public void updatePlayers() {
        updateNFLPlayersService.updatePlayersFromPythonScript();
    }
}
