package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.VehiclePricesDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Price;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PriceRepository;

import java.util.HashMap;

@Service
public class PricingService {
    private final PriceRepository priceRepository;

    public PricingService(
            PriceRepository priceRepository
    ) {
        this.priceRepository = priceRepository;
    }

    public void initPrices(){
        Price vanPrice = new Price(VehicleType.VAN, 300);
        Price standardPrice = new Price(VehicleType.STANDARD, 250);
        Price luxuryPrice = new Price(VehicleType.LUXURY, 500);

        priceRepository.save(vanPrice);
        priceRepository.save(standardPrice);
        priceRepository.save(luxuryPrice);
    }

    public void setPrices(VehiclePricesDTO prices){
        Price vanPrice = priceRepository.findById(VehicleType.VAN).orElseThrow(
                () -> new EntityNotFoundException("No price for VAN")
        );
        Price standardPrice = priceRepository.findById(VehicleType.STANDARD).orElseThrow(
                () -> new EntityNotFoundException("No price for STANDARD")
        );
        Price luxuryPrice = priceRepository.findById(VehicleType.LUXURY).orElseThrow(
                () -> new EntityNotFoundException("No price for LUXURY")
        );

        vanPrice.setPrice(prices.van);
        standardPrice.setPrice(prices.standard);
        luxuryPrice.setPrice(prices.luxury);

        priceRepository.save(vanPrice);
        priceRepository.save(standardPrice);
        priceRepository.save(luxuryPrice);
    }

    public VehiclePricesDTO getPrices(){
        Price vanPrice = priceRepository.findById(VehicleType.VAN).orElseThrow(
                () -> new EntityNotFoundException("No price for VAN")
        );
        Price standardPrice = priceRepository.findById(VehicleType.STANDARD).orElseThrow(
                () -> new EntityNotFoundException("No price for STANDARD")
        );
        Price luxuryPrice = priceRepository.findById(VehicleType.LUXURY).orElseThrow(
                () -> new EntityNotFoundException("No price for LUXURY")
        );

        VehiclePricesDTO prices = new VehiclePricesDTO();
        prices.van = vanPrice.getPrice();
        prices.standard = standardPrice.getPrice();
        prices.luxury = luxuryPrice.getPrice();

        return prices;
    }

    public double caclulatePriceForRide(Ride ride){
        double distance = ride.getRoute().getEstimatedDistanceMeters();
        double vehiclePrice = getPrices().getPriceForType(ride.getDriver().getVehicle().getType());

        return vehiclePrice + distance * 120;
    }
}
