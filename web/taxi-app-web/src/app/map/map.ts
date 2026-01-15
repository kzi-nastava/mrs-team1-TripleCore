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

  @Input() vehicleLocations: VehicleLocation[] = [];

  private locationIcon = L.icon({
    iconUrl: '/icons/location-purple.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });

  
  ngAfterViewInit(): void {
    this.initMap();
    this.renderMarkers();
  }

  ngOnChanges(changes: SimpleChanges): void {
  if (changes['vehicleLocations']) {
    if (this.map) {
      this.renderMarkers();
    }
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
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
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
      const bounds = L.latLngBounds(this.vehicleLocations.map(l => [l.latitude, l.longitude] as [number, number]));
      this.map.fitBounds(bounds, { padding: [30, 30] });
    }
  }
}
