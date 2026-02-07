import { Location } from './location';
import { VehicleType } from './vehicle-type';

export interface RideRequest {
  startLocation: Location;
  endLocation: Location;
  intermediateStops?: Location[];       
  startTime?: string;                    
  vehicleType: VehicleType; 
  babyFriendly: boolean;            
  petFriendly: boolean;
  linkedPassengerEmails?: string[];     
}