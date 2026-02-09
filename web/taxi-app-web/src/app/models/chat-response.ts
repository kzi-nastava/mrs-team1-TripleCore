export interface ChatResponse {
  chatId: number;
  userId: number;
  userName: string;
  messages: MessageResponse[];
}

export interface MessageResponse {
  text: string;
  senderId: number;
  senderRole: UserRole;
  sentAt: string; // LocalDateTime sa backenda dolazi kao ISO string
}

export enum UserRole {
  PASSENGER = 'PASSENGER',
  DRIVER = 'DRIVER',
  ADMIN = 'ADMIN'
}