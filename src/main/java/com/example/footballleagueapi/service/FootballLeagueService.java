package com.example.footballleagueapi.service;

import com.example.footballleagueapi.model.Standing;
import reactor.core.publisher.Mono;

/**
 * Interface used to get the teamStandings
 */
public interface FootballLeagueService {

    Mono<Standing> getTeamStandings(String countryName, String leagueName, String teamName);
}
