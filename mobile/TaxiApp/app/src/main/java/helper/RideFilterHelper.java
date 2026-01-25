package helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.RideDetailsDTO;

public class RideFilterHelper {

    private static final DateTimeFormatter PICKER_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static List<RideDetailsDTO> filterRides(List<RideDetailsDTO> rides,
                                                   String searchText,
                                                   String dateFromStr,
                                                   String dateToStr) {
        List<RideDetailsDTO> filtered = new ArrayList<>();

        LocalDate dateFrom = null;
        LocalDate dateTo = null;

        try {
            if (dateFromStr != null && !dateFromStr.isEmpty()) {
                dateFrom = LocalDate.parse(dateFromStr, PICKER_FORMATTER);
            }
            if (dateToStr != null && !dateToStr.isEmpty()) {
                dateTo = LocalDate.parse(dateToStr, PICKER_FORMATTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (RideDetailsDTO ride : rides) {
            boolean matchesSearch = true;
            boolean matchesDate = true;

            // Search po orderer, driver i adresama
            if (searchText != null && !searchText.isEmpty()) {
                String lowerSearch = searchText.toLowerCase();
                String orderer = ride.ordererName != null ? ride.ordererName.toLowerCase() : "";
                String driver = ride.driverName != null ? ride.driverName.toLowerCase() : "";
                String addressStart = ride.startLocation != null && ride.startLocation.address != null
                        ? ride.startLocation.address.toLowerCase() : "";
                String addressEnd = ride.endLocation != null && ride.endLocation.address != null
                        ? ride.endLocation.address.toLowerCase() : "";
                matchesSearch =
                        orderer.contains(lowerSearch) ||
                                driver.contains(lowerSearch) ||
                                addressStart.contains(lowerSearch) ||
                                addressEnd.contains(lowerSearch);
            }

            // Filter
            if ((dateFrom != null || dateTo != null) && ride.startTime != null) {
                try {
                    LocalDate rideDate = LocalDate.parse(ride.startTime.split("T")[0]); // ISO format
                    if (dateFrom != null && rideDate.isBefore(dateFrom)) matchesDate = false;
                    if (dateTo != null && rideDate.isAfter(dateTo)) matchesDate = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (matchesSearch && matchesDate) {
                filtered.add(ride);
            }
        }

        return filtered;
    }
}
