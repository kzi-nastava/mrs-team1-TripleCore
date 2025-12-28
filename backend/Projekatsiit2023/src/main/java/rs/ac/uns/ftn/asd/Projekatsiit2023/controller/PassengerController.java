package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.MakeReviewRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {
    @PostMapping("/{id}/make-review")
    public ResponseEntity<?> makeReview(
            @PathVariable("id") Long id,
            @Valid @RequestBody MakeReviewRequest request){

        String response = String.format("Review successfully created for ride %s by passenger %d", request.getRideId(), id);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable("id") Long id){
        List<ReviewDTO> response = new ArrayList<>();
        for(ReviewDTO dto : getMockReviews()){
            if (Objects.equals(dto.getPassengerId(), id)){
                response.add(dto);
            }
        }
        return ResponseEntity.ok(response);
    }

    private List<ReviewDTO> getMockReviews(){
        return List.of(
                new ReviewDTO(1L, 101L, 5, 5, "Excellent ride!"),
                new ReviewDTO(2L, 102L, 4, 5, "Good service."),
                new ReviewDTO(3L, 103L, 3, 4, "Average experience."),
                new ReviewDTO(4L, 104L, 5, 5, "Very friendly driver!"),
                new ReviewDTO(5L, 105L, 4, 4, "Comfortable ride."),
                new ReviewDTO(1L, 106L, 2, 3, "Driver was late."),
                new ReviewDTO(2L, 107L, 5, 5, "Perfect!"),
                new ReviewDTO(3L, 108L, 3, 4, "It was okay.")
        );
    }
}
