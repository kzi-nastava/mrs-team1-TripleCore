export interface NotificationResponse {
  id: number;
  recipientId: number;
  title: string;
  message: string;
  link: string;
  time: string;
  seen: boolean;
}
