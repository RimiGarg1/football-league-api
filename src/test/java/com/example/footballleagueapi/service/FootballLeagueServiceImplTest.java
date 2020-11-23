package com.example.footballleagueapi.service;


import com.example.footballleagueapi.model.Country;
import com.example.footballleagueapi.model.League;
import com.example.footballleagueapi.model.Standing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.io.IOException;
import java.util.List;

/**
 * Test class for FootballLeagueServiceImpl
 */
@ExtendWith(SpringExtension.class)
public class FootballLeagueServiceImplTest {

    private FootballLeagueServiceImpl footballLeagueService;

    private static MockWebServer mockWebServer;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException{
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        this.footballLeagueService = new FootballLeagueServiceImpl( WebClient.builder(), "http://localhost:" + mockWebServer.getPort());
        ReflectionTestUtils.setField(footballLeagueService, "apiKey", "1234");
    }

    @AfterEach
    void tearDown() throws IOException{
        mockWebServer.shutdown();
    }

    @Test
    void getCountriesTest() throws JsonProcessingException {
        //given
        List<Country> countryList = List.of(getCountryMockData());
        enqueueCountriesResponse(countryList);
        //when
        Mono<List<Country>> countriesResponse =  footballLeagueService.getCountries();
        //then
        Assert.assertNotNull(countriesResponse);
        StepVerifier.create(countriesResponse)
                .consumeNextWith(response -> assertAll("Countries Response",
                        () -> assertNotNull(response),
                        () -> assertEquals(1, response.get(0).getCountryId()),
                        () -> assertEquals("England", response.get(0).getCountryName()),
                        () -> assertEquals(1, response.size())))
                .verifyComplete();
    }

    private void enqueueCountriesResponse(List<Country> countryList) throws JsonProcessingException {
        MockResponse response = new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(OBJECT_MAPPER.writeValueAsString(countryList));
        mockWebServer.enqueue(response);
    }

    public static Country getCountryMockData(){
        return Country.builder()
                .countryId(1)
                .countryName("England")
                .build();
    }

    @Test
    void getLeaguesTest() throws JsonProcessingException{
        //given
        List <League> leagueList = List.of(getLeagueyMockData());
        enqueueLeaguesResponse(leagueList);
        //when
        Mono <List<League>> leaguesResponse =  footballLeagueService.getLeagues(1);
        //then
        Assert.assertNotNull(leaguesResponse);
        StepVerifier.create(leaguesResponse)
                .consumeNextWith(response -> assertAll("Leagues Response",
                        () -> assertNotNull(response),
                        () -> assertEquals(1, response.get(0).getCountryId()),
                        () -> assertEquals("England", response.get(0).getCountryName()),
                        () -> assertEquals(1, response.get(0).getLeagueId()),
                        () -> assertEquals("Premier League", response.get(0).getLeagueName()),
                        () -> assertEquals(1, response.size())))
                .verifyComplete();
    }

    private void enqueueLeaguesResponse(List<League> leagueList) throws  JsonProcessingException{
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(OBJECT_MAPPER.writeValueAsString(leagueList));
        mockWebServer.enqueue(mockResponse);
    }

    public static League getLeagueyMockData(){
        return League.builder()
                .countryId(1)
                .countryName("England")
                .leagueId(1)
                .leagueName("Premier League")
                .build();
    }

    @Test
    void getStandingsTest() throws JsonProcessingException{
        //given
        List <Standing> standingList = List.of(getStandingMockData());
        enqueueStandingsResponse(standingList);
        //when
        Mono <List<Standing>> standingsResponse =  footballLeagueService.getStandings(1);
        //then
        Assert.assertNotNull(standingsResponse);
        StepVerifier.create(standingsResponse)
                .consumeNextWith(response -> assertAll("Standings Response",
                        () -> assertNotNull(response),
                        //   () -> assertEquals(1, response.get(0).getCountryId()),
                        () -> assertEquals("England", response.get(0).getCountryName()),
                        () -> assertEquals(1, response.get(0).getLeagueId()),
                        () -> assertEquals("Premier League", response.get(0).getLeagueName()),
                        () -> assertEquals(1, response.get(0).getTeamId()),
                        () -> assertEquals("Bournemouth", response.get(0).getTeamName()),
                        () -> assertEquals(1, response.get(0).getOverallLeaguePostion()),
                        () -> assertEquals(1, response.size())))
                .verifyComplete();
    }

    private void enqueueStandingsResponse(List<Standing> standingList) throws JsonProcessingException{
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(OBJECT_MAPPER.writeValueAsString(standingList));
        mockWebServer.enqueue(mockResponse);
    }

    public static Standing getStandingMockData(){
        return Standing.builder()
                .countryName("England")
                .leagueId(1)
                .leagueName("Premier League")
                .teamId(1)
                .teamName("Bournemouth")
                .overallLeaguePostion(1)
                .build();
    }

    @Test
    void getTeamStandingsTest() throws JsonProcessingException {
        //given
        List <Country> countryList = List.of(getCountryMockData());
        enqueueCountriesResponse(countryList);
        List <League> leagueList = List.of(getLeagueyMockData());
        enqueueLeaguesResponse(leagueList);
        List <Standing> standingList = List.of(getStandingMockData());
        enqueueStandingsResponse(standingList);
        //when
        Mono<Standing> teamStandingResponse = footballLeagueService.getTeamStandings("England" , "Premier League" , "Bournemouth");

        //then
        Assert.assertNotNull(teamStandingResponse);
        StepVerifier.create(teamStandingResponse)
                .consumeNextWith(response -> assertAll("Standings Response",
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

}
