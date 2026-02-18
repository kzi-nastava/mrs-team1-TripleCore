package rs.ac.uns.ftn.asd.Projekatsiit2023.services.driving;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DrivingScheduler {

    private final DrivingSimulationService drivingSimulationService;

    @Scheduled(fixedRate = 5000)
    public void moveAllVehicles(){
        drivingSimulationService.moveAllIdleVehicles();
        drivingSimulationService.moveAllBusyVehicles();
    }
}
