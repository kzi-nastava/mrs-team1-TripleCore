package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.review.CreateReviewRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.exceptions.ReviewMappingException;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Review;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ReviewRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PassengerRepository passengerRepository;
    private final RideRepository rideRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            PassengerRepository passengerRepository,
            RideRepository rideRepository){
        this.reviewRepository = reviewRepository;
        this.passengerRepository = passengerRepository;
        this.rideRepository = rideRepository;
    }

    public void CreateReview(CreateReviewRequest request){
        try{
            Passenger passenger = passengerRepository.findById(request.getPassengerId())
                    .orElseThrow(() -> new RuntimeException("Passenger not found"));
            Ride ride = rideRepository.findById(request.getRideId())
                    .orElseThrow(() -> new RuntimeException("Ride not found"));

            Review review = new Review();
            review.setPassenger(passenger);
            review.setRide(ride);
            review.setDriverRating(request.getDriverRating());
            review.setVehicleRating(request.getVehicleRating());
            review.setComment(request.getComment());

            reviewRepository.save(review);

        } catch (Exception ex){
            throw new ReviewMappingException("Failed mapping request to review");
        }
    }

    public List<Review> getRideReviews(Long rideId){
        return reviewRepository.findByRideId(rideId);
    }

    public ReviewDTO GenerateReviewDTO(Review review){
        ReviewDTO dto = new ReviewDTO();
        try{
            Passenger passenger = review.getPassenger();
            Ride ride = review.getRide();
            Driver driver = ride.getDriver();

            dto.setRideId(ride.getId());
            dto.setPassengerId(passenger.getId());
            dto.setPassengerName(passenger.getFirstName() + " " + passenger.getLastName());
            dto.setDriverId(driver.getId());
            dto.setDriver(driver.getFirstName() + " " + driver.getLastName());
            dto.setDriverRating(review.getDriverRating());
            dto.setVehicleRating(review.getVehicleRating());
            dto.setComment(review.getComment());
            return dto;
        } catch (Exception e){
            throw new ReviewMappingException("Failed mapping review to ReviewPresentationDTO");
        }
    }
}
