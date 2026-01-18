export interface RideCancelResponse {
  success: boolean;
  cancelledBy: 'DRIVER' | 'PASSENGER';
  reason?: string;
}
