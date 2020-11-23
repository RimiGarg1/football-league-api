package com.example.footballleagueapi.controller;

import com.example.footballleagueapi.model.Standing;
import com.example.footballleagueapi.service.FootballLeagueServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/**
 * Test class for FootballLeagueController
 */
@ExtendWith(SpringExtension.class)
public class FootballLeagueControllerTest {

    private FootballLeagueController footballLeagueController;

    @MockBean
    private FootballLeagueServiceImpl footballLeagueService;

    @BeforeEach
    void setUp() {
        this.footballLeagueController = new FootballLeagueController(footballLeagueService);
    }

    @AfterEach
    void tearDown() {
        footballLeagueController = null;
    }

    @Test
    void getTeamStandingsTest()  {
        //given
       given(footballLeagueService.getTeamStandings("England" , "Premier League" , "Bournemouth"))
               .willReturn(Mono.just(getStandingMockData()));
        //when
        Mono <Standing> teamStanding =  footballLeagueController.getTeamStandings("England" , "Premier League" , "Bournemouth");
        //then
        Assert.assertNotNull(teamStanding);
        StepVerifier.create(teamStanding)
                .consumeNextWith(response -> assertAll("Team Standing Response",
                        () -> assertNotNull(response),
                        () -> assertEquals(1, response.getCountryId()),
                        () -> assertEquals("England", response.getCountryName()),
                        () -> assertEquals(1, response.getLeagueId()),
                        () -> assertEquals("Premier League", response.getLeagueName()),
                        () -> assertEquals(1, response.getTeamId()),
                        () -> assertEquals("Bournemouth", response.getTeamName()),
                        () -> assertEquals(1, response.getOverallLeaguePostion())
                        ))
                .verifyComplete();
    }

    private static Standing getStandingMockData(){
        return Standing.builder()
                .countryId(1)
                .countryName("England")
                .leagueId(1)
                .leagueName("Premier League")
                .teamId(1)
                .teamName("Bournemouth")
                .overallLeaguePostion(1)
                .build();
    }

}
