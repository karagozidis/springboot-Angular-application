import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketToolsComponent } from './market-tools.component';

describe('MarketToolsComponent', () => {
  let component: MarketToolsComponent;
  let fixture: ComponentFixture<MarketToolsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketToolsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketToolsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
