import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRideDetailsComponent } from './admin-ride-details';

describe('AdminRideDetails', () => {
  let component: AdminRideDetailsComponent;
  let fixture: ComponentFixture<AdminRideDetailsComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminRideDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRideDetailsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
