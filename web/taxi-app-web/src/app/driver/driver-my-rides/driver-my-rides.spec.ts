import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverMyRides } from './driver-my-rides';

describe('DriverMyRides', () => {
  let component: DriverMyRides;
  let fixture: ComponentFixture<DriverMyRides>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverMyRides]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverMyRides);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
