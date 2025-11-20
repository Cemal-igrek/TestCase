package utils;

public class FlightData {
    private String airline;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String connectionInfo;
    private double price;

    public FlightData(String airline, String departureTime, String arrivalTime, String duration, String connectionInfo, double price) {
        this.airline = airline;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.connectionInfo = connectionInfo;
        this.price = price;
    }

    public String[] toStringArray() {
        return new String[]{airline, departureTime, arrivalTime, duration, connectionInfo, String.valueOf(price)};
    }

    public String getAirline() { return airline; }
    public double getPrice() { return price; }
    public String getDepartureTime() { return departureTime; }

    public double getCostScore() {
        return price;
    }
}