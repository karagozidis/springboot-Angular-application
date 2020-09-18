import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveSubstationsManagementComponent } from './active-substations-management.component';

describe('ActiveSubstationsManagementComponent', () => {
  let component: ActiveSubstationsManagementComponent;
  let fixture: ComponentFixture<ActiveSubstationsManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActiveSubstationsManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveSubstationsManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
