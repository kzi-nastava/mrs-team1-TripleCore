import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerRideDetails } from './passenger-ride-details';

describe('PassengerRideDetails', () => {
  let component: PassengerRideDetails;
  let fixture: ComponentFixture<PassengerRideDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PassengerRideDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerRideDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
