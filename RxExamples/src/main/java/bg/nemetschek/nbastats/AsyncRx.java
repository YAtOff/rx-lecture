package bg.nemetschek.nbastats;

import bg.nemetschek.nbastats.entity.Player;
import bg.nemetschek.nbastats.service.NBAServiceRx;
import bg.nemetschek.nbastats.service.ServiceFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import rx.Observable;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsyncRx {
    public static void main(String[] args) {
        String teamName = "Warriors";
        Retrofit retrofit = ServiceFactory.retrofit()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        NBAServiceRx service = retrofit.create(NBAServiceRx.class);

        long startTime = System.currentTimeMillis();
        service.getTeams()
                .flatMap(teams -> Observable.from(
                        teams.stream()
                                .filter(t -> t.getName().equals(teamName))
                                .limit(1)
                                .collect(Collectors.toList()))
                )
                .flatMap(team -> service.getTeam(team.getCode()))
                .flatMap(team -> Observable.from(team.getPlayers())
                        .flatMap(code -> service.getPlayer(team.getCode(), code)))
                .reduce((top, player) -> Stream.of(top, player)
                        .max(Comparator.comparing(p -> p.getStats().getPoints()))
                        .get()
                )
                .subscribe(
                        top -> {
                            System.out.println(String.format("Player with max points is %s with %2.1f PTS",
                                    top.getName(), top.getStats().getPoints()));
                            System.out.println(String.format("Time: %d", System.currentTimeMillis() - startTime));
                        },
                        Throwable::printStackTrace
                );
    }
}
