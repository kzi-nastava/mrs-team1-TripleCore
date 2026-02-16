package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideReportResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideReportSummaryResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideReportService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RideReportServiceImpl implements RideReportService {
    private final RideRepository rideRepository;

    public RideReportServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }


    public List<RideReportResponse> getRideReportForUser(User user, LocalDate startDate, LocalDate endDate) {
        List<Ride> rides;

        if (user.getRole().name().equals("ADMIN")) {
            rides = rideRepository.findAll();
        } else if (user.getRole().name().equals("DRIVER")) {
            rides = rideRepository.findByDriverId(user.getId());
        } else {
            // Passenger
            rides = rideRepository.findByPassengerId(user.getId());
        }


        rides = rides.stream()
                .filter(r -> !r.getStartTime().toLocalDate().isBefore(startDate)
                        && !r.getStartTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());


        Map<LocalDate, List<Ride>> ridesByDay = rides.stream()
                .collect(Collectors.groupingBy(r -> r.getStartTime().toLocalDate()));

        List<RideReportResponse> report = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Ride>> entry : ridesByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<Ride> dayRides = entry.getValue();
            int count = dayRides.size();
            double totalDistance = dayRides.stream().mapToDouble(r -> r.getRoute().getEstimatedDistanceMeters()).sum();
            double totalPrice = dayRides.stream().mapToDouble(r -> r.getPrice() != null ? r.getPrice() : 0).sum();
            report.add(new RideReportResponse(date, count, totalDistance, totalPrice));
        }


        report.sort(Comparator.comparing(RideReportResponse::getDate));
        return report;
    }


    public RideReportSummaryResponse getRideReportSummary(User user, LocalDate startDate, LocalDate endDate) {
        List<RideReportResponse> dailyReports = getRideReportForUser(user, startDate, endDate);
        int totalRides = dailyReports.stream().mapToInt(RideReportResponse::getRideCount).sum();
        double totalDistance = dailyReports.stream().mapToDouble(RideReportResponse::getTotalDistance).sum();
        double totalPrice = dailyReports.stream().mapToDouble(RideReportResponse::getTotalPrice).sum();
        double averageDistance = totalRides > 0 ? totalDistance / totalRides : 0;
        double averagePrice = totalRides > 0 ? totalPrice / totalRides : 0;
        return new RideReportSummaryResponse(totalDistance, totalPrice, totalRides, averageDistance, averagePrice);
    }

    @Override
    public List<RideReportResponse> getRideReportForAllUsers(
            LocalDate startDate,
            LocalDate endDate) {

        List<Ride> rides =
                rideRepository.findByStartTimeBetween(
                        startDate.atStartOfDay(),
                        endDate.atTime(23,59)
                );

        Map<LocalDate, List<Ride>> grouped =
                rides.stream()
                        .collect(Collectors.groupingBy(
                                r -> r.getStartTime().toLocalDate()
                        ));

        List<RideReportResponse> result = new ArrayList<>();

        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {

            List<Ride> dayRides =
                    grouped.getOrDefault(current, new ArrayList<>());

            int count = dayRides.size();

            double distance = dayRides.stream()
                    .mapToDouble(r -> r.getRoute() != null ? r.getRoute().getEstimatedDistanceMeters() : 0)
                    .sum();

            double price = dayRides.stream()
                    .mapToDouble(r -> r.getPrice() != null ? r.getPrice() : 0)
                    .sum();

            result.add(new RideReportResponse(
                    current,
                    count,
                    distance,
                    price
            ));

            current = current.plusDays(1);
        }

        return result;
    }


}
