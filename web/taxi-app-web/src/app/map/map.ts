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

  private vehicleIcon = L.icon({
    iconUrl: '/icons/location-purple.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
  });

  private startIcon = L.divIcon({
    html: '<div style="background-color: green; width: 12px; height: 12px; border-radius: 50%; border: 2px solid white;"></div>',
    className: '',
    iconSize: [12, 12],
    iconAnchor: [6, 6]
  });

  private endIcon = L.divIcon({
    html: '<div style="background-color: red; width: 12px; height: 12px; border-radius: 50%; border: 2px solid white;"></div>',
    className: '',
    iconSize: [12, 12],
    iconAnchor: [6, 6]
  });

  ngAfterViewInit(): void {
    this.initMap();
    this.renderVehicleMarkers();

    if (this.routeData) {
      this.drawRoute();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['vehicleLocations'] && this.map) {
      this.renderVehicleMarkers();
    }
    if (changes['routeData'] && this.map && this.routeData) {
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
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);
  }

  private renderVehicleMarkers(): void {
    if (this.markersLayer) this.map.removeLayer(this.markersLayer);

    this.markersLayer = L.layerGroup(
      this.vehicleLocations.map(loc => L.marker([loc.latitude, loc.longitude], { icon: this.vehicleIcon }))
    );

    this.markersLayer.addTo(this.map);
  }

  private drawRoute(): void {
    if (this.routeLine) this.map.removeLayer(this.routeLine);
    if (this.startMarker) this.map.removeLayer(this.startMarker);
    if (this.endMarker) this.map.removeLayer(this.endMarker);

    if (!this.routeData?.routeCoordinates) return;

    const coordinates = this.routeData.routeCoordinates
      .split(';')
      .map((pair: { split: (arg0: string) =>
         { (): any; new(): any; map: { (arg0: NumberConstructor): [any, any]; new(): any; }; }; }) => {
        const [lat, lon] = pair.split(',').map(Number);
        return [lat, lon] as [number, number];
      });

    if (coordinates.length < 2) return;

    this.routeLine = L.polyline(coordinates, { color: '#2563EB', weight: 4 }).addTo(this.map);

    this.startMarker = L.marker(coordinates[0], { icon: this.startIcon }).addTo(this.map);
    this.endMarker = L.marker(coordinates[coordinates.length - 1], { icon: this.endIcon }).addTo(this.map);

    this.map.fitBounds(this.routeLine.getBounds().pad(0.1));
  }
}
