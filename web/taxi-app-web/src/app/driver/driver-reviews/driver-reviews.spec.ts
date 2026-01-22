import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverReviewsComponent } from './driver-reviews';

describe('DriverReviewsComponent', () => {
  let component: DriverReviewsComponent;
  let fixture: ComponentFixture<DriverReviewsComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverReviewsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverReviewsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
