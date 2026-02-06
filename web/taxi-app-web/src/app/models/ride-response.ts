import { Location } from './location';
import { RideStatus } from './ride-status';

export interface RideResponse {
  rideId: number;
  estimatedEndTime: string;  // ISO string, jer Date se šalje JSON-om
  vehicleId: number;
  driverName: string;
  routePoints: Location[];
  status: RideStatus;
}