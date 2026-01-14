import { Component, AfterViewInit, Input, OnChanges, SimpleChanges} from '@angular/core';
import * as L from 'leaflet';

export interface MapLocation {
  lat: number;
  lng: number;
  label?: string;
}

@Component({
  selector: 'app-map',
  templateUrl: './map.html',
  styleUrls: ['./map.css'],
  standalone: true,
})
export class MapComponent implements AfterViewInit, OnChanges {

  private map: any;
  private markersLayer = L.layerGroup(); // layer that shows vehicle location markers

  @Input() route: any;
  @Input() vehicleLocations: MapLocation[] = []; // this input holds vehicle locations to be displayed on the map
  private locationIcon = L.icon({
    iconUrl: '/icons/location-purple.png',
    iconSize: [32, 32],       
    iconAnchor: [16, 32],     
    popupAnchor: [0, -32],    
  });

  constructor() {}

  private initMap(): void {
    this.map = L.map('map', {
      center: [45.2396, 19.8227],
      zoom: 13,
    });

    const tiles = L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 18,
        minZoom: 3,
        attribution:
          '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }
    );
    tiles.addTo(this.map);
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.markersLayer.addTo(this.map);
    this.renderMarkers();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['vehicleLocations'] && this.map) {
      this.renderMarkers();
    }
  }

  private renderMarkers(): void {
    this.markersLayer.clearLayers();

    this.vehicleLocations.forEach(loc => {
      const marker = L.marker(
        [loc.lat, loc.lng],
        { icon: this.locationIcon }
      );

      if (loc.label) {
        marker.bindPopup(loc.label);
      }

      marker.addTo(this.markersLayer);
    });

    if (this.vehicleLocations.length > 0) { // Adjust map view to fit all markers
      const bounds = L.latLngBounds(
        this.vehicleLocations.map(l => [l.lat, l.lng] as [number, number])
      );
      this.map.fitBounds(bounds, { padding: [30, 30] });
    }
  }
}
