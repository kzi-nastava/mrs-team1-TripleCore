import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveRideTrackingComponent } from './active-ride-tracking';

describe('ActiveRideTrackingComponent', () => {
  let component: ActiveRideTrackingComponent;
  let fixture: ComponentFixture<ActiveRideTrackingComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActiveRideTrackingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveRideTrackingComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
