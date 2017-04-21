package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import bg.nemetschek.nbastats.util.FixedSortedList;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Callback {
    public static void main(String[] args) {
        FixedSortedList<Player> bestShooters = new FixedSortedList<>(
                10,
                Comparator.comparingDouble(player -> player.getStats().getPoints())
        );

        final CountDownLatch globalLatch = new CountDownLatch(2);
        Client client = ClientBuilder.newClient()
                .register(JacksonJsonProvider.class);
        WebTarget target = client.target("http://localhost:8080");

        long startTime = System.currentTimeMillis();
        target
            .path("teams")
            .request(MediaType.APPLICATION_JSON)
            .async()
            .get(new InvocationCallback<List<Team>>() {
                @Override
                public void completed(final List<Team> teams) {
                    globalLatch.countDown();

                    final CountDownLatch teamFetchLatch = new CountDownLatch(teams.size());
                    for (Team team : teams) {
                        target
                            .path(String.format("teams/%s/players", team.getCode()))
                            .request(MediaType.APPLICATION_JSON)
                            .async()
                            .get(new InvocationCallback<List<Player>>() {
                                @Override
                                public void completed(final List<Player> players) {
                                    players.forEach(bestShooters::tryAdd);
                                    teamFetchLatch.countDown();
                                    if (teamFetchLatch.getCount() == 0) {
                                        globalLatch.countDown();
                                    }
                                }
                                @Override
                                public void failed(Throwable throwable) {
                                    System.out.println("Invocation failed.");
                                    throwable.printStackTrace();
                                    teamFetchLatch.countDown();
                                    if (teamFetchLatch.getCount() == 0) {
                                        globalLatch.countDown();
                                    }
                                }
                            });
                    }
                }

                @Override
                public void failed(Throwable throwable) {
                    System.out.println("Invocation failed.");
                    throwable.printStackTrace();
                    globalLatch.countDown();
                    globalLatch.countDown();
                }
            });

        try {
            if (!globalLatch.await(10, TimeUnit.SECONDS)) {
                System.out.println("Outer: Waiting for requests to complete has timed out.");
            }
            System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));
            for (Player player : bestShooters) {
                System.out.println(String.format("%s %s %2.1f", player.getName(), player.getTeam(), player.getStats().getPoints()));
            }
        } catch (final InterruptedException e) {
            System.out.println("Outer: Waiting for requests to complete has been interrupted.");
        }
    }
}
