export interface PanicAlert {
  id: number;
  driverName: string;
  passengerName: string;
  time: string; 
  resolved: boolean;
  vehicle: string;
  location: string;
  licensePlate?: string;
}