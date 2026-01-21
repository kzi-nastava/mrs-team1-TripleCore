import { RideDetailsResponse, RideStatus } from '../models/ride-details-response';

export interface PassengerRide {
  id: number;
  driverName: string;
  driverImage: string;
  driverRating: number;
  vehicleModel: string;
  vehicleType: string;
  licensePlate: string;
  babyFriendly: boolean;
  petFriendly: boolean;
  pickup: string;
  destination: string;
  date: Date;
  scheduledTime: string;
  estimatedEnd: string;
  duration: number;
  price: number;
  status: RideStatus;
  isRated: boolean;
  panic: boolean;
  notes: string;
}

export function adaptToPassengerRide(backendRide: RideDetailsResponse): PassengerRide {
  const formatTime = (dateTimeString: string): string => {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return date.toLocaleTimeString('sr-RS', { 
      hour: '2-digit', 
      minute: '2-digit',
      hour12: false 
    });
  };

  const calculateDuration = (start: string, end: string): number => {
    if (!start || !end) return 0;
    const startDate = new Date(start);
    const endDate = new Date(end);
    const diffMs = endDate.getTime() - startDate.getTime();
    return Math.round(diffMs / 60000);
  };

  const extractVehicleInfo = (vehicleString: string) => {
    if (!vehicleString) return { model: '', type: 'STANDARD', plate: '' };
    
    const plateMatch = vehicleString.match(/\(([^)]+)\)$/);
    const plate = plateMatch ? plateMatch[1] : '';
    const model = plateMatch ? vehicleString.replace(plateMatch[0], '').trim() : vehicleString;
    
    return {
      model: model || 'Unknown',
      type: model.includes('LUXURY') ? 'LUXURY' : 
            model.includes('VAN') ? 'VAN' : 'STANDARD',
      plate: plate
    };
  };

  const vehicleInfo = extractVehicleInfo(backendRide.vehicle || '');

  return {
    id: backendRide.id || 0,
    driverName: backendRide.driverName || 'Unknown Driver',
    driverImage: backendRide.driverProfileImage || 'icons/profile.png',
    driverRating: 4.5, // TODO: Calculate based on reviews
    vehicleModel: vehicleInfo.model,
    vehicleType: vehicleInfo.type,
    licensePlate: vehicleInfo.plate,
    babyFriendly: false, // TODO: Add to backend
    petFriendly: false,  // TODO: Add to backend
    pickup: backendRide.startLocation?.address || 'Unknown location',
    destination: backendRide.endLocation?.address || 'Unknown location',
    date: new Date(backendRide.startTime || Date.now()),
    scheduledTime: formatTime(backendRide.startTime),
    estimatedEnd: formatTime(backendRide.endTime),
    duration: calculateDuration(backendRide.startTime, backendRide.endTime),
    price: backendRide.price || 0,
    status: backendRide.status || 'REQUESTED',
    isRated: (backendRide.reviews && backendRide.reviews.length > 0) || false,
    panic: backendRide.panic || false,
    notes: backendRide.inconsistencies || ''
  };
}