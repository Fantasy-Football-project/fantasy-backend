package com.sathvik.dto;

import com.sathvik.entities.League;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;
    //private List<League> leagues = new ArrayList<>();
}
