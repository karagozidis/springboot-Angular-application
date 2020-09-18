import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveSubstationsUploadForecastedDataComponent } from './active-substations-upload-forecasted-data.component';

describe('ActiveSubstationsUploadForecastedDataComponent', () => {
  let component: ActiveSubstationsUploadForecastedDataComponent;
  let fixture: ComponentFixture<ActiveSubstationsUploadForecastedDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActiveSubstationsUploadForecastedDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveSubstationsUploadForecastedDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
