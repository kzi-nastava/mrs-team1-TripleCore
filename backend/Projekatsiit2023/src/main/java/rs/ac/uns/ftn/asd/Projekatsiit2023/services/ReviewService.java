package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewPresentationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.exceptions.ReviewMappingException;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Review;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository rr){
        this.reviewRepository = rr;
    }

    public ReviewPresentationDTO GenerateReviewPresentation(Review review){
        ReviewPresentationDTO dto = new ReviewPresentationDTO();
        try{
            Passenger passenger = review.getPassenger();
            Driver driver = review.getDriver();
            dto.setPassenger(passenger.getFirstName() + " " + passenger.getLastName());
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
