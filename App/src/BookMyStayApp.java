import java.util.*;

/* Service class */
class Service {
    private String name;
    private double price;

    public Service(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

/* Add-On Service Manager */
class AddOnServiceManager {

    // reservationId -> list of services
    private Map<String, List<Service>> servicesByReservation;

    // constructor
    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    // attach service
    public void addService(String reservationId, Service service) {

        servicesByReservation
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service Added -> Reservation: "
                + reservationId +
                ", Service: " + service.getName());
    }

    // calculate total cost
    public double calculateTotalServiceCost(String reservationId) {

        List<Service> services =
                servicesByReservation.get(reservationId);

        if (services == null)
            return 0.0;

        double total = 0;

        for (Service s : services) {
            total += s.getPrice();
        }

        return total;
    }
}

/* Main class */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Add-On Service Selection");

        AddOnServiceManager manager =
                new AddOnServiceManager();

        // sample reservation IDs
        String r1 = "R101";
        String r2 = "R102";

        // services
        Service breakfast = new Service("Breakfast", 500);
        Service wifi = new Service("WiFi", 200);
        Service pickup = new Service("Airport Pickup", 1200);
        Service spa = new Service("Spa", 1500);

        // attach services
        manager.addService(r1, breakfast);
        manager.addService(r1, wifi);
        manager.addService(r1, spa);

        manager.addService(r2, pickup);
        manager.addService(r2, wifi);

        // calculate totals
        double total1 =
                manager.calculateTotalServiceCost(r1);

        double total2 =
                manager.calculateTotalServiceCost(r2);

        System.out.println("\nTotal Add-on Cost for "
                + r1 + " : " + total1);

        System.out.println("Total Add-on Cost for "
                + r2 + " : " + total2);
    }
}
