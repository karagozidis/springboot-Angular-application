import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricalMarketDataComponent } from './historical-market-data.component';

describe('HistoricalMarketDataComponent', () => {
  let component: HistoricalMarketDataComponent;
  let fixture: ComponentFixture<HistoricalMarketDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoricalMarketDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricalMarketDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
