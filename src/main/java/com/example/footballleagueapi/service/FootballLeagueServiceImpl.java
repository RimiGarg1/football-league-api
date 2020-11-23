package com.example.footballleagueapi.service;

import com.example.footballleagueapi.model.Country;
import com.example.footballleagueapi.model.League;
import com.example.footballleagueapi.model.Standing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class used to call Countries, Leagues and Standing API to get the Required response based on Country name, league name and team name
 */
@Service
@PropertySource("classpath:application.properties")
public class FootballLeagueServiceImpl implements FootballLeagueService {

    private  final WebClient webClient;

    @Value("${api.football.data.management.key}")
    private String apiKey;

    public FootballLeagueServiceImpl(WebClient.Builder webClientBuilder,
                                     @Value("${api.football.data.management.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl)
                .build();
    }

    public Mono<List<Country>> getCountries(){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "get_countries")
                        .queryParam("APIkey",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Country>>() {
                })
                .doOnSuccess(countries -> System.out.println("countries success: "+countries.size()+ countries.get(0).getCountryId()))
                .doOnError(exception -> Mono.error(new RuntimeException("Error while retrieving countries"+ exception.getMessage())));
    }

    public Mono<List<League>> getLeagues(int countryId){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "get_leagues")
                        .queryParam("country_id",countryId)
                        .queryParam("APIkey",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<League>>() {
                })
                .doOnSuccess(leagues -> System.out.println("league success: "+leagues.size()))
                .doOnError(exception -> Mono.error(new RuntimeException("Error while retrieving leagues"+ exception.getMessage())));
    }

    public Mono<List<Standing>> getStandings(int leagueId){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "get_standings")
                        .queryParam("league_id",leagueId)
                        .queryParam("APIkey",apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Standing>>() {
                })
                .doOnSuccess(standings -> System.out.println("standing success: "+standings.size()))
                .doOnError(exception -> Mono.error(new RuntimeException("Error while retrieving standings"+ exception.getMessage())));
    }

    @Override
    public Mono < Standing > getTeamStandings ( String countryName , String leagueName , String teamName ){
        AtomicReference<Country> countryAtomicReference = new AtomicReference<>();
        return getCountries()
                .map(countries -> countries.stream()
                        .filter((country -> country.getCountryName().equalsIgnoreCase(countryName)))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No matching country found")))
                .flatMap(country ->{
                    countryAtomicReference.set(country);
                    System.out.println("League"+ country.getCountryId());
                    return getLeagues(country.getCountryId())
                            .map(leagues -> leagues.stream()
                                    .filter(league -> league.getLeagueName().equalsIgnoreCase(leagueName))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("No matching league found")));
                })
                .flatMap(league -> getStandings(league.getLeagueId())
                        .map(standings -> standings.stream()
                                .filter(standing -> standing.getTeamName().equalsIgnoreCase(teamName))
                                .findFirst()
                                .map(standing ->  {
                                    standing.setCountryId(countryAtomicReference.get().getCountryId()
                                          );
                                    return standing;
                                })
                                .orElseThrow(() -> new RuntimeException("No matching standing found"))));
    }

}
