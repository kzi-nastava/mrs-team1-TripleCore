package helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.RideDetailsDTO;

public class RideFilterHelper {

    private static final DateTimeFormatter PICKER_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static List<RideDetailsDTO> filterRides(
            List<RideDetailsDTO> rides,
            String searchText,
            String dateFromStr,
            String dateToStr
    ) {
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

            // Text search
            if (searchText != null && !searchText.isEmpty()) {
                String lowerSearch = searchText.toLowerCase();

                String orderer = ride.ordererName != null ? ride.ordererName.toLowerCase() : "";
                String driver = ride.driverName != null ? ride.driverName.toLowerCase() : "";
                String start = ride.startLocation != null && ride.startLocation.address != null
                        ? ride.startLocation.address.toLowerCase() : "";
                String end = ride.endLocation != null && ride.endLocation.address != null
                        ? ride.endLocation.address.toLowerCase() : "";

                matchesSearch =
                        orderer.contains(lowerSearch) ||
                                driver.contains(lowerSearch) ||
                                start.contains(lowerSearch) ||
                                end.contains(lowerSearch);
            }

            // Date filter
            if ((dateFrom != null || dateTo != null) && ride.startTime != null) {
                try {
                    LocalDate rideDate =
                            LocalDate.parse(ride.startTime.split("T")[0]);

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

    public static List<RideDetailsDTO> filterAndSortRides(
            List<RideDetailsDTO> rides,
            String searchText,
            String dateFromStr,
            String dateToStr,
            boolean sortDescending
    ) {

        List<RideDetailsDTO> filtered = filterRides(rides, searchText, dateFromStr, dateToStr);

        filtered.sort((r1, r2) -> {
            if (r1.startTime == null || r2.startTime == null) return 0;
            return sortDescending
                    ? r2.startTime.compareTo(r1.startTime)
                    : r1.startTime.compareTo(r2.startTime);
        });

        return filtered;
    }
}
