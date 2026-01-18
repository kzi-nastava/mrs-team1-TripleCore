import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderRideRegisteredUser } from './order-ride-registered-user';

describe('OrderRideRegisteredUser', () => {
  let component: OrderRideRegisteredUser;
  let fixture: ComponentFixture<OrderRideRegisteredUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderRideRegisteredUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderRideRegisteredUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
