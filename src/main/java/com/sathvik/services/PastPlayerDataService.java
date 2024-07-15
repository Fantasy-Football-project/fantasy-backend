package com.sathvik.services;

import com.sathvik.entities.PastPlayerData;
import com.sathvik.repositories.PastPlayerDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PastPlayerDataService {
    private final PastPlayerDataRepository pastPlayerDataRepository;

    public List<PastPlayerData> getAllPastPlayerData() {
        return pastPlayerDataRepository.findAll();
    }

    public List<PastPlayerData> getPlayerData(String playerName) {
        return pastPlayerDataRepository.findByPlayerName(playerName);
    }

    public List<PastPlayerData> getDSTData(String teamName) {
        return pastPlayerDataRepository.findByTeamName(teamName);
    }
}
