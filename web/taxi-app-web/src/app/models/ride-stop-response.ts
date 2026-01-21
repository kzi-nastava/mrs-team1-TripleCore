export interface RideStopResponse {
  success: boolean;
  message: string;
  newTotalPrice: number;   
  newDistance: number;
  stopTime: string;        
  finalAddress: string;   
}
