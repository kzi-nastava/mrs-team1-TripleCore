import { ReviewDTO } from './review-dto';

export interface RideDetailsResponse {
  id: number;
  ordererName: string;

  ordererProfileImage: string | null;
  driverProfileImage: string | null;

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

export type RideStatus =
  | 'REQUESTED'
  | 'ACCEPTED'
  | 'REJECTED'
  | 'IN_PROGRESS'
  | 'CANCELLED'
  | 'FINISHED';
