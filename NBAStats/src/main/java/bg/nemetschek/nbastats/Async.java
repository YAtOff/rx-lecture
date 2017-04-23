package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.entity.Team;
import bg.nemetschek.nbastats.service.ServiceFactory;
import bg.nemetschek.nbastats.service.NBAService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

public class Async {
    public static void main(String[] args) {
        String teamName = "Warriors";
        Retrofit retrofit = ServiceFactory.retrofit().build();
        NBAService service = retrofit.create(NBAService.class);

        List<Player> players = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        service.getTeams().enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                try {
                    String teamCode = Utils.findTeam(response.body(), teamName).getCode();
                    service.getTeam(teamCode).enqueue(new Callback<Team>() {
                        @Override
                        public void onResponse(Call<Team> call, Response<Team> response) {
                            Team team = response.body();
                            int playersCount = team.getPlayers().size();
                            for (String playerCode : team.getPlayers()) {
                                service.getPlayer(teamCode, playerCode).enqueue(new Callback<Player>() {
                                    @Override
                                    public void onResponse(Call<Player> call, Response<Player> response) {
                                        players.add(response.body());
                                        if (players.size() == playersCount) {
                                            try {
                                                Player top = Utils.topPlayer(players);
                                                System.out.println(String.format("Player with max points is %s with %2.1f PTS",
                                                        top.getName(), top.getStats().getPoints()));
                                                System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));
                                                System.exit(0);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Player> call, Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Team> call, Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Team>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }
}

