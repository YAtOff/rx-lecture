package bg.nemetschek.nbastats.entity;

import java.util.List;

public class Team {
    private String code;
    private String name;
    private String city;
    private List<String> players;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return String.format("Team(%s, %s, %s)", getCode(), getName(), getCity());
    }
}
