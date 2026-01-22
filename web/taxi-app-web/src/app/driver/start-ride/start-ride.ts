import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-start-ride',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './start-ride.html',
  styleUrl: './start-ride.css',
})
export class StartRideComponent {
  @Input() activeRide: any = {
    startName: 'Kneginje Milice 12',
    destName: 'Bulevar Oslobođenja 45',
    stations: ['Stražilovska 10', 'Nikole Pašića 5'], 
  };

  @Output() rideStartedEvent = new EventEmitter<void>();

  onStartRideClick() {
    console.log('Ride started');
    alert('Ride has started!');
    this.rideStartedEvent.emit();
  }
}