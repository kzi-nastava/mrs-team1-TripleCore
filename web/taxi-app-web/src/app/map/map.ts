import { Component, AfterViewInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import * as L from 'leaflet';
import { VehicleLocation } from '../models/vehicle-location';

@Component({
  selector: 'app-map',
  standalone: true,
  templateUrl: './map.html',
  styleUrls: ['./map.css'],
})
export class MapComponent implements AfterViewInit, OnChanges {

  private map!: L.Map;
  private markersLayer?: L.LayerGroup;
  private routeLine?: L.Polyline;
  private startMarker?: L.Marker;
  private endMarker?: L.Marker;

  @Input() vehicleLocations: VehicleLocation[] = [];
  @Input() routeData: any = null;

  private locationIcon = L.icon({
    iconUrl: '/icons/location-purple.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });

  private startIcon = L.divIcon({
    html: '<div style="background-color: #10B981; width: 16px; height: 16px; border-radius: 50%; border: 2px solid white;"></div>',
    className: 'custom-div-icon',
    iconSize: [16, 16],
    iconAnchor: [8, 8]
  });

  private endIcon = L.divIcon({
    html: '<div style="background-color: #EF4444; width: 16px; height: 16px; border-radius: 50%; border: 2px solid white;"></div>',
    className: 'custom-div-icon',
    iconSize: [16, 16],
    iconAnchor: [8, 8]
  });

  ngAfterViewInit(): void {
    this.initMap();
    this.renderMarkers();

    if (this.routeData) {
      this.drawRoute();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['vehicleLocations'] && this.map) {
      this.renderMarkers();
    }

    if (changes['routeData'] && this.map) {
      this.drawRoute();
    }
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [45.2396, 19.8227],
      zoom: 13,
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);
  }

  private renderMarkers(): void {
    if (this.markersLayer) {
      this.map.removeLayer(this.markersLayer);
    }

    this.markersLayer = L.layerGroup(
      this.vehicleLocations.map(loc => {
        const marker = L.marker([loc.latitude, loc.longitude], { icon: this.locationIcon });
        marker.bindPopup(loc.available ? 'Available' : 'Not Available');
        return marker;
      })
    );

    this.markersLayer.addTo(this.map);

    if (this.vehicleLocations.length > 0) {
      const bounds = L.latLngBounds(
        this.vehicleLocations.map(l => [l.latitude, l.longitude] as [number, number])
      );
      this.map.fitBounds(bounds, { padding: [30, 30] });
    }
  }

  private getShortAddress(fullAddress: string): string {
    if (!fullAddress) return 'N/A';
    const parts = fullAddress.split(',');
    // take first two parts for short address
    return parts.slice(0, 2).join(', ');
  }

  private drawRoute(): void {
    this.clearRoute();

    if (!this.routeData || !this.routeData.routeCoordinates) return;

    const coordinates = this.parseRouteCoordinates(this.routeData.routeCoordinates);
    if (coordinates.length < 2) return;

    // Route line
    this.routeLine = L.polyline(coordinates, { color: '#3B82F6', weight: 4 }).addTo(this.map);

    // Start marker + tooltip
    const startPoint = coordinates[0];
    this.startMarker = L.marker(startPoint, { icon: this.startIcon })
      .addTo(this.map)
      .bindTooltip(
        `From: ${this.getShortAddress(this.routeData.startAddress)}<br>` +
        `To: ${this.getShortAddress(this.routeData.endAddress)}<br>` +
        `Distance: ${this.routeData.distance?.toFixed(1)} km<br>` +
        `Time: ${this.routeData.estimatedTime} min`,
        { permanent: true, direction: 'top', offset: [0, -10], className: 'route-tooltip' }
      );

    // End marker
    const endPoint = coordinates[coordinates.length - 1];
    this.endMarker = L.marker(endPoint, { icon: this.endIcon }).addTo(this.map);

    // Fit map bounds
    const bounds = L.latLngBounds(coordinates);
    this.map.fitBounds(bounds.pad(0.1));
  }

  private parseRouteCoordinates(coordString: string): [number, number][] {
    return coordString
      ? coordString.split(';').map(pair => {
          const [lat, lng] = pair.split(',').map(Number);
          return [lat, lng] as [number, number];
        })
      : [];
  }

  private clearRoute(): void {
    if (this.routeLine) { this.map.removeLayer(this.routeLine); this.routeLine = undefined; }
    if (this.startMarker) { this.map.removeLayer(this.startMarker); this.startMarker = undefined; }
    if (this.endMarker) { this.map.removeLayer(this.endMarker); this.endMarker = undefined; }
  }
}
