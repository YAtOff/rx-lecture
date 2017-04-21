package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import bg.nemetschek.nbastats.util.FixedSortedList;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.Comparator;
import java.util.List;

public class Sync {
    public static void main(String[] args) {
        FixedSortedList<Player> bestShooters = new FixedSortedList<>(
            10,
            Comparator.comparingDouble(player -> player.getStats().getPoints())
        );

        Client client = ClientBuilder.newClient()
                .register(JacksonJsonProvider.class);
        WebTarget target = client.target("http://localhost:8080");

        long startTime = System.currentTimeMillis();
        List<Team> teams = target
                .path("teams")
                .request()
                .get(new GenericType<List<Team>>() {});
        for (Team team : teams) {
            List<Player> players = target
                .path(String.format("teams/%s/players", team.getCode()))
                .request()
                .get(new GenericType<List<Player>>() {});
            players.forEach(bestShooters::tryAdd);
        }
        System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));

        for (Player player : bestShooters) {
            System.out.println(String.format("%s %s %2.1f", player.getName(), player.getTeam(), player.getStats().getPoints()));
        }
    }
}
