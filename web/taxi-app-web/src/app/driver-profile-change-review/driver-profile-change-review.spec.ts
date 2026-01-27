import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverProfileChangeReviewComponent } from './driver-profile-change-review';

describe('DriverProfileChangeReviewComponent', () => {
  let component: DriverProfileChangeReviewComponent;
  let fixture: ComponentFixture<DriverProfileChangeReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverProfileChangeReviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverProfileChangeReviewComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
