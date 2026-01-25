export interface UserProfileResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  address: string;
  phone: string;
  profileImage: string | null;
}