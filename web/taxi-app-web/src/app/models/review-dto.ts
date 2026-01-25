export interface ReviewDTO {
    id: number;
    passengerId: number;
    passengerName: string;
    driverId: number;
    driverName: string;
    driverRating: number;
    vehicleRating: number;
    comment: string;
}