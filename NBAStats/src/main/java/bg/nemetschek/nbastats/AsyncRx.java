package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.service.NBAServiceRx;
import bg.nemetschek.nbastats.service.ServiceFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import rx.Observable;

import java.util.ArrayList;

public class AsyncRx {
    public static void main(String[] args) {
        String teamName = "Warriors";
        Retrofit retrofit = ServiceFactory.retrofit()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        NBAServiceRx service = retrofit.create(NBAServiceRx.class);

        long startTime = System.currentTimeMillis();
        service.getTeams()
                .flatMap(teams -> {
                    try {
                        String teamCode = Utils.findTeam(teams, teamName).getCode();
                        return service.getTeam(teamCode);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(team -> Observable.from(team.getPlayers())
                        .flatMap(code -> service.getPlayer(team.getCode(), code)))
                .reduce(new ArrayList<Player>(), (acc, player) -> {
                    acc.add(player);
                    return acc;
                })
                .subscribe(
                        players -> {
                            try {
                                Player top = Utils.topPlayer(players);
                                System.out.println(String.format("Player with max points is %s with %2.1f PTS",
                                        top.getName(), top.getStats().getPoints()));
                                System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        Throwable::printStackTrace
                );
    }
}
