package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.service.NBAServiceFeature;
import bg.nemetschek.nbastats.service.ServiceFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncFuture {
    public static void main(String[] args) {
        String teamName = "Warriors";
        Retrofit retrofit = ServiceFactory.retrofit()
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .build();
        NBAServiceFeature service = retrofit.create(NBAServiceFeature.class);

        long startTime = System.currentTimeMillis();
        // get teams
        service.getTeams()
                .thenCompose(teams -> {
                    try {
                        // find team
                        String teamCode = Utils.findTeam(teams, teamName).getCode();
                        // get team
                        return service.getTeam(teamCode);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                // get players
                .thenCompose(team -> {
                    List<CompletableFuture<Player>> futures = team.getPlayers().stream()
                            .map(code -> service.getPlayer(team.getCode(), code))
                            .collect(Collectors.toList());
                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                            .thenApply(empty -> futures.stream()
                                    .map(f -> {
                                        try {
                                            return f.get();
                                        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
                                            // should not happen
                                            return null;
                                        }
                                    })
                                    .collect(Collectors.toList())
                            );
                })
                // find top player
                .thenAccept(players -> {
                    try {
                        Player top = Utils.topPlayer(players);
                        System.out.println(String.format("Player with max points is %s with %2.1f PTS",
                                top.getName(), top.getStats().getPoints()));
                        System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

}
