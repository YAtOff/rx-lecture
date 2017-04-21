package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import bg.nemetschek.nbastats.util.FixedSortedList;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
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

        List<Team> teams = client.target("http://localhost:8080")
                .path("teams")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Team>>() {});
        for (Team team : teams) {
            List<Player> players = client.target("http://localhost:8080")
                .path(String.format("teams/%s/players", team.getCode()))
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Player>>() {});
            for (Player player : players) {
                bestShooters.tryAdd(player);
            }
        }

        for (Player player : bestShooters) {
            System.out.println(String.format("%s %s %2.1f", player.getName(), player.getTeam(), player.getStats().getPoints()));
        }
    }
}
