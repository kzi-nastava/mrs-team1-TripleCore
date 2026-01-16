export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  address: string;
  phoneNumber: string;
  profileImage?: string; 
  role?: string;  // default is passenger
}