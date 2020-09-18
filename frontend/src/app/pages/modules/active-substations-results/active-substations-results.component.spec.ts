import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveSubstationsResultsComponent } from './active-substations-results.component';

describe('ActiveSubstationsResultsComponent', () => {
  let component: ActiveSubstationsResultsComponent;
  let fixture: ComponentFixture<ActiveSubstationsResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActiveSubstationsResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveSubstationsResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
