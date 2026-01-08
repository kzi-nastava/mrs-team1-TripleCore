import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerMyRidesComponent } from './passenger-my-rides';

describe('PassengerMyRidesComponent', () => {
  let component: PassengerMyRidesComponent;
  let fixture: ComponentFixture<PassengerMyRidesComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PassengerMyRidesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerMyRidesComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
