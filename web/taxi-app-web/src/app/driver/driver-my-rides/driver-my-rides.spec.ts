import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverMyRidesComponent } from './driver-my-rides';

describe('DriverMyRides', () => {
  let component: DriverMyRidesComponent;
  let fixture: ComponentFixture<DriverMyRidesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverMyRidesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverMyRidesComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
