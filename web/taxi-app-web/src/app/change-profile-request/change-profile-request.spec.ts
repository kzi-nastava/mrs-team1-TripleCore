import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeProfileRequestComponent } from './change-profile-request';

describe('ChangeProfileRequest', () => {
  let component: ChangeProfileRequestComponent;
  let fixture: ComponentFixture<ChangeProfileRequestComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChangeProfileRequestComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangeProfileRequestComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
