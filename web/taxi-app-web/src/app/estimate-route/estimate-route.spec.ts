import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstimateRoute } from './estimate-route';

describe('EstimateRoute', () => {
  let component: EstimateRoute;
  let fixture: ComponentFixture<EstimateRoute>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EstimateRoute]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstimateRoute);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
