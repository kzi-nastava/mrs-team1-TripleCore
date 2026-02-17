import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminReportComponent } from './admin-report';

describe('AdminReportComponent', () => {
  let component: AdminReportComponent;
  let fixture: ComponentFixture<AdminReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminReportComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
