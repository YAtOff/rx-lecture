package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;

import java.util.Comparator;
import java.util.List;

public class Utils {
    public static Player topPlayer(List<Player> players) throws Exception {
        return players.stream()
                .max(Comparator.comparing(p -> p.getStats().getPoints()))
                .orElseThrow(() -> new Exception("No players in team"));
    }

    public static Team findTeam(List<Team> teams,String name) throws Exception {
        return teams.stream()
                .filter(t -> t.getName().equals(name)).findFirst()
                .orElseThrow(() -> new Exception("Team not found"));
    }
}
