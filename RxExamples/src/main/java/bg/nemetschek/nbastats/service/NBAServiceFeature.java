package bg.nemetschek.nbastats.service;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NBAServiceFeature {
    @GET("/teams")
    CompletableFuture<List<Team>> getTeams();

    @GET("/teams/{team_code}")
    CompletableFuture<Team> getTeam(@Path("team_code") String teamCode);

    @GET("/teams/{team_code}/players/{player_code}")
    CompletableFuture<Player> getPlayer(@Path("team_code") String teamCode, @Path("player_code") String playerCode);
}
