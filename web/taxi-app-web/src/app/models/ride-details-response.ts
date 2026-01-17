export interface RideDetailsResponse {
  ordererName: string;
  linkedPassengers: string[];
  driverName: string;
  vehicle: string;

  startLocation: LocationDTO;
  endLocation: LocationDTO;
  routeStops: LocationDTO[];

  startTime: string; 
  endTime: string;   

  panic: boolean;
  panicTriggeredBy: string | null;
  panicTriggeredAt: string | null;

  price: number;
  status: RideStatus;

  cancelledBy: string | null;
  reviews: ReviewDTO[];
  inconsistencies: string | null;
}

export interface LocationDTO {
  latitude: number;
  longitude: number;
  address: string;
}

export interface ReviewDTO {
    passenger: string;
    driver: string;
    driverRating: number;
    vehicleRating: number;
    comment: string;
}

export type RideStatus =
  | 'CREATED'
  | 'ACCEPTED'
  | 'STARTED'
  | 'FINISHED'
  | 'CANCELLED';
