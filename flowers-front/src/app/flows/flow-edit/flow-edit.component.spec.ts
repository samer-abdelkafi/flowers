import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlowEditComponent } from './flow-edit.component';

describe('FlowEditComponent', () => {
  let component: FlowEditComponent;
  let fixture: ComponentFixture<FlowEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FlowEditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FlowEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
