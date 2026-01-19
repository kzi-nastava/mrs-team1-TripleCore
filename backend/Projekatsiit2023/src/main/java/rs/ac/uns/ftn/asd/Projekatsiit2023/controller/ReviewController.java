package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.review.CreateReviewRequest;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {
    @PostMapping("/create")
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewRequest request){
        return ResponseEntity.ok("Dobijen zahtev." + request.getComment());
    }
}
