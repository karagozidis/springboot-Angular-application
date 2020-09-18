import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WeatherImportComponent } from './weather-import.component';

describe('WeatherImportComponent', () => {
  let component: WeatherImportComponent;
  let fixture: ComponentFixture<WeatherImportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WeatherImportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WeatherImportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
