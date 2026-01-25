import { RideDetailsResponse } from '../models/ride-details-response';

export const MOCK_RIDE_DETAILS: RideDetailsResponse = {
  id: 1,
  ordererName: 'Marko Marković',

  ordererProfileImage: null,
  driverProfileImage: 'https://example.com/images/driver.jpg',

  linkedPassengers: ['Ana Anić', 'Jovan Jovanović'],
  driverName: 'Petar Petrović',
  vehicle: 'Toyota Corolla - BG 123-AB',

  startLocation: {
    latitude: 45.2671,
    longitude: 19.8335,
    address: 'Bulevar oslobođenja 1, Novi Sad'
  },
  endLocation: {
    latitude: 45.2550,
    longitude: 19.8450,
    address: 'Trg slobode, Novi Sad'
  },
  routeStops: [
    {
      latitude: 45.2600,
      longitude: 19.8400,
      address: 'Futoška 10, Novi Sad'
    }
  ],

  startTime: '2026-01-20T14:30:00',
  endTime: '2026-01-20T14:55:00',

  panic: false,
  panicTriggeredBy: null,
  panicTriggeredAt: null,

  price: 850,
  status: 'FINISHED',

  cancelledBy: null,

  reviews: [
    {
      id: 1,
      passengerId: 101,
      passengerName: 'Ana Anić',
      driverId: 501,
      driverName: 'Petar Petrović',
      driverRating: 5,
      vehicleRating: 4,
      comment: 'Vožnja je bila prijatna i bez problema.'
    }
  ],

  inconsistencies: null
};
