import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketDemoDesignComponent } from './market-demo-design.component';

describe('MarketDemoDesignComponent', () => {
  let component: MarketDemoDesignComponent;
  let fixture: ComponentFixture<MarketDemoDesignComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketDemoDesignComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketDemoDesignComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
