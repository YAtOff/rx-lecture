package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import bg.nemetschek.nbastats.service.ServiceFactory;
import bg.nemetschek.nbastats.service.NBAService;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

public class Sync {
    public static void main(String[] args) {
        String teamName = "Warriors";
        Retrofit retrofit = ServiceFactory.retrofit().build();
        NBAService service = retrofit.create(NBAService.class);

        try {
            long startTime = System.currentTimeMillis();
            List<Team> teams = service.getTeams().execute().body();
            String teamCode = Utils.findTeam(teams, teamName).getCode();
            Team team = service.getTeam(teamCode).execute().body();
            List<Player> players = new ArrayList<>(team.getPlayers().size());
            for (String playerCode : team.getPlayers()) {
                Player player = service.getPlayer(teamCode, playerCode).execute().body();
                players.add(player);
            }
            Player top = Utils.topPlayer(players);
            System.out.println(String.format("Player with max points is %s with %2.1f PTS",
                    top.getName(), top.getStats().getPoints()));
            System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
