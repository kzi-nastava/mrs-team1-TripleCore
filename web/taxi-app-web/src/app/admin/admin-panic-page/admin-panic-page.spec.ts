import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPanicPage } from './admin-panic-page';

describe('AdminPanicPage', () => {
  let component: AdminPanicPage;
  let fixture: ComponentFixture<AdminPanicPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPanicPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminPanicPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
