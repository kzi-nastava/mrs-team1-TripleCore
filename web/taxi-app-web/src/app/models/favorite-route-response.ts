export interface FavoriteRouteResponse {
  id: number;

  startAddress: string;
  startLat: number;
  startLon: number;

  endAddress: string;
  endLat: number;
  endLon: number;

  estimatedDistanceMeters: number;
  estimatedDurationSeconds: number;
}
