package com.example.footballleagueapi.controller;

import com.example.footballleagueapi.model.Standing;
import com.example.footballleagueapi.service.FootballLeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Class will receive the Web client request and send back the response in Json format
 */

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class FootballLeagueController {

    private final FootballLeagueService footballLeagueService;

    @GetMapping("/standings")
    public Mono<Standing> getTeamStandings(
            @RequestParam(value = "countryName") String countryName,
            @RequestParam(value = "leagueName") String leagueName,
            @RequestParam(value = "teamName") String teamName
    ){
        return footballLeagueService.getTeamStandings(countryName, leagueName, teamName);
    }

}
