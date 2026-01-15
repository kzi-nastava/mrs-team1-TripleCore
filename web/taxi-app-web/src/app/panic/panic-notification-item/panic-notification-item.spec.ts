import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PanicNotificationItemComponent } from './panic-notification-item';

describe('PanicNotificationItem', () => {
  let component: PanicNotificationItemComponent;
  let fixture: ComponentFixture<PanicNotificationItemComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PanicNotificationItemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PanicNotificationItemComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
