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


  ngAfterViewInit(): void {
    this.initMap();
    this.renderMarkers();
    this.loadRoute();
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
        const marker = L.marker([loc.latitude, loc.longitude], { icon: this.redIcon });
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

}
