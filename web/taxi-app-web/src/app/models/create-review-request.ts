export interface CreateReviewRequest {
  passengerId: number;
  rideId: number;
  driverRating: number;
  vehicleRating: number;
  comment: string;
}