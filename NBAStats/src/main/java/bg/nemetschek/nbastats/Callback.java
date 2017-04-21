package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import bg.nemetschek.nbastats.util.FixedSortedList;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.tools.corba.se.idl.ExceptionEntry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Callback {
    public static void main(String[] args) {
        FixedSortedList<Player> bestShooters = new FixedSortedList<>(
                10,
                Comparator.comparingDouble(player -> player.getStats().getPoints())
        );

        Client client = ClientBuilder.newClient()
                .register(JacksonJsonProvider.class);

        Future<List<Team>> teamsFuture = client.target("http://localhost:8080")
                .path("teams")
                .request(MediaType.APPLICATION_JSON)
                .async()
                .get(new InvocationCallback<List<Team>>() {
                    @Override
                    public void completed(final List<Team> teams) {
                        final CountDownLatch teamFetchLatch = new CountDownLatch(teams.size());
                        for (Team team : teams) {
                            client.target("http://localhost:8080")
                                .path(String.format("teams/%s/players", team.getCode()))
                                .request(MediaType.APPLICATION_JSON)
                                .async()
                                .get(new InvocationCallback<List<Player>>() {
                                    @Override
                                    public void completed(final List<Player> players) {
                                        for (Player player : players) {
                                            bestShooters.tryAdd(player);
                                        }
                                        teamFetchLatch.countDown();
                                    }
                                    @Override
                                    public void failed(Throwable throwable) {
                                        System.out.println("Invocation failed.");
                                        throwable.printStackTrace();
                                    }
                                });
                            System.out.println(team);
                        }
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        System.out.println("Invocation failed.");
                        throwable.printStackTrace();
                    }
                });
        try {
            teamsFuture.get();
        } catch (InterruptedException | ExecutionException e) {

        }

    }
}
