import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerCancelRideDialogComponent } from './passenger-cancel-ride-dialog';

describe('PassengerCancelRideDialogComponent', () => {
  let component: PassengerCancelRideDialogComponent;
  let fixture: ComponentFixture<PassengerCancelRideDialogComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PassengerCancelRideDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerCancelRideDialogComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
