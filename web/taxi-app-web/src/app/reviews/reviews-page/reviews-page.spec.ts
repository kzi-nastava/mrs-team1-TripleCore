import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewsPageComponent } from './reviews-page';

describe('ReviewsPageComponent', () => {
  let component: ReviewsPageComponent;
  let fixture: ComponentFixture<ReviewsPageComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewsPageComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
