import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewFormComponent } from './review-form';

describe('ReviewFormComponent', () => {
  let component: ReviewFormComponent;
  let fixture: ComponentFixture<ReviewFormComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
