import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockUserComponent } from './block-user';

describe('BlockUserComponent', () => {
  let component: BlockUserComponent;
  let fixture: ComponentFixture<BlockUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlockUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BlockUserComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
