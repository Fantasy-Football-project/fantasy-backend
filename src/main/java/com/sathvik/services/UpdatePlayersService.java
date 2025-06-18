package com.sathvik.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdatePlayersService {
    public void updatePlayersFromPythonScript() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "/path/to/your/script.py");
            pb.inheritIO(); // for logs to be shown in console
            Process process = pb.start();
            process.waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
