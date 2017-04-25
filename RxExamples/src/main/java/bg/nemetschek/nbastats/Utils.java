package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import rx.Observable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    public static Player topPlayer(List<Player> players) throws Exception {
        return players.stream()
                .max(Comparator.comparing(p -> p.getStats().getPoints()))
                .orElseThrow(() -> new Exception("No players in team"));
    }

    public static Team findTeam(List<Team> teams, String name) throws Exception {
        return teams.stream()
                .filter(t -> t.getName().equals(name)).findFirst()
                .orElseThrow(() -> new Exception("Team not found"));
    }

    public static Stream<Team> findTeam(Stream<Team> teams, String name) {
        return teams.filter(team -> team.getName().equals(name)).limit(1);
    }

}
