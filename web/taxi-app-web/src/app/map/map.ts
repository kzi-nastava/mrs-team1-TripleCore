import { Component, AfterViewInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import * as L from 'leaflet';
import { VehicleLocation } from '../models/vehicle-location';
import { LocationDTO } from '../models/ride-details-response';
import { HttpClient } from '@angular/common/http';
import { GeoRoutingService } from '../services/geo-routing-service';

@Component({
  selector: 'app-map',
  standalone: true,
  templateUrl: './map.html',
  styleUrls: ['./map.css'],
})
export class MapComponent implements AfterViewInit, OnChanges {

  constructor(private geoRoutingService: GeoRoutingService) {}
  
  private map!: L.Map;
  private markersLayer?: L.LayerGroup;
  private routeLayer?: L.GeoJSON;

  @Input() vehicleLocations: VehicleLocation[] = [];
  @Input() routeStart!: LocationDTO;
  @Input() routeEnd!: LocationDTO;
  @Input() routeStops: LocationDTO[] = [];
  private routeLine?: L.Polyline;
  private startMarker?: L.Marker;
  private endMarker?: L.Marker;

  @Input() routeData: any = null;

  private redIcon = L.icon({
    iconUrl: '/icons/location-red.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });

  private blueIcon = L.icon({
    iconUrl: '/icons/location-blue.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });

  private greenIcon = L.icon({
    iconUrl: '/icons/location-green.png',
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
    // this.renderMarkers();
    // this.loadRoute();

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
        const marker = L.marker([loc.latitude, loc.longitude], { icon: this.redIcon });
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

  private loadRoute(): void {
  this.geoRoutingService
    .getRoute(this.routeStart, this.routeStops, this.routeEnd)
    .subscribe(geometry => {

      if (this.routeLayer) {
        this.map.removeLayer(this.routeLayer);
        this.map.removeLayer(this.markersLayer!);
      }

      this.routeLayer = L.geoJSON(geometry, {
        style: { weight: 4 }
      }).addTo(this.map);

      const marker = L.marker([this.routeStart.latitude, this.routeStart.longitude], { icon: this.redIcon });
      marker.bindPopup('Start');

      const endMarker = L.marker([this.routeEnd.latitude, this.routeEnd.longitude], { icon: this.greenIcon });
      endMarker.bindPopup('End');

      const stopMarkers = this.routeStops.map(stop => {
        const stopMarker = L.marker([stop.latitude, stop.longitude], { icon: this.blueIcon });
        stopMarker.bindPopup('Route Stop');
        return stopMarker;
      });

      this.markersLayer = L.layerGroup([marker, endMarker, ...stopMarkers]).addTo(this.map);
      this.map.addLayer(this.markersLayer);

      this.map.fitBounds(this.routeLayer.getBounds());
    });
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
