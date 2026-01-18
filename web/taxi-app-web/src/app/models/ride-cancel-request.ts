export interface RideCancelRequest {
  cancelerType: 'DRIVER' | 'PASSENGER';
  reason?: string;
}