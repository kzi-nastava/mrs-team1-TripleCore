import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerReviewsComponent } from './passenger-reviews';

describe('PassengerReviewsComponent', () => {
  let component: PassengerReviewsComponent;
  let fixture: ComponentFixture<PassengerReviewsComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PassengerReviewsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerReviewsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
