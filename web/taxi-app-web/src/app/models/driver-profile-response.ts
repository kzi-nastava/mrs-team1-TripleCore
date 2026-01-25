import { UserProfileResponse } from "./user-profile-response";

export enum VehicleType {
  "STANDARD",
  "LUXURY",
  "VAN"
}

export interface DriverProfileResponse extends UserProfileResponse {
  workingHoursToday: number;
  vehicle: {
    id: number;
    brand: string;
    model: string;
    plateNumber: string;
    seatNumber: number;
    babyFriendly: boolean;
    petFriendly: boolean;
    type: VehicleType;
  }
}