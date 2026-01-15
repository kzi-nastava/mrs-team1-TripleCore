import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PanicNotificationsComponent } from './panic-notifications';

describe('PanicNotifications', () => {
  let component: PanicNotificationsComponent;
  let fixture: ComponentFixture<PanicNotificationsComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PanicNotificationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PanicNotificationsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
