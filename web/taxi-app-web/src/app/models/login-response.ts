export interface LoginResponse {
  id: number;
  email: string;
  role: 'DRIVER' | 'ADMIN' | 'PASSENGER';
  firstName: string;
  lastName: string;
  token: string;
  driverAvailable?: boolean;
}