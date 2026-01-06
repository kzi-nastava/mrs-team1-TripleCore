import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRideHistoryTableComponent } from './admin-ride-history-table';

describe('AdminRideHistoryTableComponent', () => {
  let component: AdminRideHistoryTableComponent;
  let fixture: ComponentFixture<AdminRideHistoryTableComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminRideHistoryTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRideHistoryTableComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
