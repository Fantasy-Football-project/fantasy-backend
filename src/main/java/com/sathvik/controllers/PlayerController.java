package com.sathvik.controllers;

import com.sathvik.entities.Player;
import com.sathvik.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PlayerController {
    private final PlayerService playerService;


}
