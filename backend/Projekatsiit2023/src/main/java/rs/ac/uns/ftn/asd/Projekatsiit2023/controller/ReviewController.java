package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.review.CreateReviewRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.ReviewService;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewRequest request){
        try{
            reviewService.CreateReview(request);
            return ResponseEntity.ok("Review created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @GetMapping("/passenger/{id}")
//    public ResponseEntity<?> getPassengerReviews(@PathVariable("id") Long id){
//
//    }

//    @GetMapping("/driver/{id}")
//    public ResponseEntity<?> getDriverReviews(@PathVariable("id") Long id){
//
//    }
}
