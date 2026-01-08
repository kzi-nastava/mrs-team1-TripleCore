import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverCancelRideDialogComponent } from './driver-cancel-ride-dialog';

describe('DriverCancelRideDialogComponent', () => {
  let component: DriverCancelRideDialogComponent;
  let fixture: ComponentFixture<DriverCancelRideDialogComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverCancelRideDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverCancelRideDialogComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
