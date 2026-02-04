import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerNotificationsComponent } from './passenger-notifications';

describe('PassengerNotifications', () => {
  let component: PassengerNotificationsComponent;
  let fixture: ComponentFixture<PassengerNotificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PassengerNotificationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerNotificationsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
