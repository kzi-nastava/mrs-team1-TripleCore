import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverAdditionalInfo } from './driver-additional-info';

describe('DriverAdditionalInfo', () => {
  let component: DriverAdditionalInfo;
  let fixture: ComponentFixture<DriverAdditionalInfo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DriverAdditionalInfo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverAdditionalInfo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
