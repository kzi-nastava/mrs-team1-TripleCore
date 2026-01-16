export interface RegisterResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  address: string;
  phoneNumber: string;
  profilePicture: string | null;
  role: string;
  activated: boolean;
  message: string;
}