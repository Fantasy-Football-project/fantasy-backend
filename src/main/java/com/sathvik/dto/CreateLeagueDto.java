package com.sathvik.dto;

import com.sathvik.entities.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeagueDto {
    private String leagueName;
    //private ArrayList<Team> teams;
    private int numTeams;
    private boolean ppr;
    private boolean nonPPR;
    private boolean halfPPR;

    public boolean getPpr() {
        return ppr;
    }

    public boolean getNonPPR() {
        return nonPPR;
    }

    public boolean getHalfPPR() {
        return halfPPR;
    }

    private String username;
}
