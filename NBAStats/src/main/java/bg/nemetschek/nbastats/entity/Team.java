package bg.nemetschek.nbastats.entity;

public class Team {
    private String code;
    private String name;
    private String city;

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

    @Override
    public String toString() {
        return String.format("Tema(%s, %s, %s)", getCode(), getName(), getCity());
    }
}
