export interface RouteResponse {
  estimatedTime: number;
  distance: number;
  routeCoordinates: string;
  message: string;
  centerLat?: number;
  centerLon?: number;
}