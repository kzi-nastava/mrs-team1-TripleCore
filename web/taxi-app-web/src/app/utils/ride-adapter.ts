import { RideDetailsResponse, RideStatus } from '../models/ride-details-response';

export interface FrontendRide {
  id: number;
  passengerName: string;
  passengerImage: string;
  passengerRating: number;
  pickup: string;
  destination: string;
  date: Date;
  scheduledTime: string;
  estimatedEnd: string;
  duration: number;
  price: number;
  status: RideStatus;
  panic: boolean;
  vehicleType: string;
  notes: string;
}

export function adaptToFrontendRide(backendRide: RideDetailsResponse): FrontendRide {
  const formatTime = (dateTimeString: string): string => {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return date.toLocaleTimeString('sr-RS', { hour: '2-digit', minute: '2-digit' });
  };

  const calculateDuration = (start: string, end: string): number => {
    if (!start || !end) return 0;
    const startDate = new Date(start);
    const endDate = new Date(end);
    const diffMs = endDate.getTime() - startDate.getTime();
    return Math.round(diffMs / 60000); 
  };

  const calculatePassengerRating = (reviews: any[]): number => {
    if (!reviews || reviews.length === 0) return 4.5; 
    const ratings = reviews.map(r => r.driverRating || 0);
    const average = ratings.reduce((a, b) => a + b, 0) / ratings.length;
    return Math.round(average * 10) / 10; 
  };

  return {
    id: backendRide.id,
    passengerName: backendRide.ordererName || 'Unknown',
    passengerImage: 'icons/profile.png',
    passengerRating: calculatePassengerRating(backendRide.reviews || []),
    pickup: backendRide.startLocation?.address || 'Unknown location',
    destination: backendRide.endLocation?.address || 'Unknown location',
    date: new Date(backendRide.startTime || Date.now()),
    scheduledTime: formatTime(backendRide.startTime),
    estimatedEnd: formatTime(backendRide.endTime),
    duration: calculateDuration(backendRide.startTime, backendRide.endTime),
    price: backendRide.price || 0,
    status: backendRide.status,
    panic: backendRide.panic || false,
    vehicleType: backendRide.vehicle || 'STANDARD',
    notes: backendRide.inconsistencies || ''
  };
}