package bg.nemetschek.nbastats.service;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

import java.util.List;

public interface NBAServiceRx {
    @GET("/teams")
    Observable<List<Team>> getTeams();

    @GET("/teams/{team_code}")
    Observable<Team> getTeam(@Path("team_code") String teamCode);

    @GET("/teams/{team_code}/players/{player_code}")
    Observable<Player> getPlayer(@Path("team_code") String teamCode, @Path("player_code") String playerCode);
}
