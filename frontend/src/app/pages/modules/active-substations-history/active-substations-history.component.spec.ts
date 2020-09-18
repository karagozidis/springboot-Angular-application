import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveSubstationsHistoryComponent } from './active-substations-history.component';

describe('ActiveSubstationsHistoryComponent', () => {
  let component: ActiveSubstationsHistoryComponent;
  let fixture: ComponentFixture<ActiveSubstationsHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActiveSubstationsHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveSubstationsHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
