package group1.tcss450.uw.edu.messageappgroup1.model;

public class Coord {
    private float lon;
    private float lat;

    public Coord() {
    }

    public float getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return new StringBuilder("[").append(this.lat).append(',').append(this.lon).append(']').toString();
    }
}
