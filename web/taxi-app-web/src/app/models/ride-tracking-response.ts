import { LocationDTO } from "./ride-details-response";

export interface RideTrackingResponse {
  rideId: number;
  vehicleId: number;
  vehicleLocation: LocationDTO;
  estimatedTime: number; // in seconds
  estimatedDistance: number; // in meters
}