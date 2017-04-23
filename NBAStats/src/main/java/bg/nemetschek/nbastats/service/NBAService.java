package bg.nemetschek.nbastats.service;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface NBAService {
    @GET("/teams")
    Call<List<Team>> getTeams();

    @GET("/teams/{team_code}")
    Call<Team> getTeam(@Path("team_code") String teamCode);

    @GET("/teams/{team_code}/players/{player_code}")
    Call<Player> getPlayer(@Path("team_code") String teamCode, @Path("player_code") String playerCode);
}
