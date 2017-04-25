package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.service.NBAService;
import bg.nemetschek.nbastats.service.ServiceFactory;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Stream;

public class SyncStream {
    public static void main(String[] args) {
        String teamName = "Warriors";
        Retrofit retrofit = ServiceFactory.retrofit().build();
        NBAService service = retrofit.create(NBAService.class);

        try {
            long startTime = System.currentTimeMillis();
            Player top = service.getTeams().execute().body().stream()
                    .filter(t -> t.getName().equals(teamName))
                    .limit(1)
                    .flatMap(team -> {
                        try {
                            return Stream.of(service.getTeam(team.getCode()).execute().body());
                        } catch (IOException e) {
                            return Stream.empty();
                        }
                    })
                    .flatMap(team ->
                            team.getPlayers().stream()
                                .map(playerCode -> {
                                    try {
                                        return service.getPlayer(team.getCode(), playerCode).execute().body();
                                    } catch (IOException e) {
                                        return Stream.empty();
                                    }
                                })
                    )
                    .max(Comparator.comparing(player -> ((Player) player).getStats().getPoints()))
                    .map(o -> (Player) o)
                    .orElseThrow(() -> new Exception("No players in team"));

            System.out.println(String.format("Player with max points is %s with %2.1f PTS",
                    top.getName(), top.getStats().getPoints()));
            System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
