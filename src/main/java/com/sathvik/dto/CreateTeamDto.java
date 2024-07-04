package com.sathvik.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateTeamDto {

    //When creating a team one of these fields is required, as the league is either
    //identified by its name (when creating a league) or a joinCode (when joining one).
    private String leagueName;
    private String joinCode;

    private String teamName;

    private String fullName;

    private String username;
}
