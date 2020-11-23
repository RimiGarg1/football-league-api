package com.example.footballleagueapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for Standings API
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Standing {

    @JsonProperty("country_id")
    private int countryId;
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("league_id")
    private int leagueId;
    @JsonProperty("league_name")
    private String leagueName;
    @JsonProperty("team_id")
    private int teamId;
    @JsonProperty("team_name")
    private String teamName;
    @JsonProperty("overall_league_position")
    private int overallLeaguePostion;
   }
