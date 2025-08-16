package journey.dto;

import java.util.List;

public class MultiTripRequest {
    private List<TripLeg> trips;
    private String cabinClass;

    // getter & setter

    public static class TripLeg {
        private String departure;
        private String destination;

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        private String date;

        // getter & setter
    }

    public List<TripLeg> getTrips() {
        return trips;
    }

    public void setTrips(List<TripLeg> trips) {
        this.trips = trips;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

}
