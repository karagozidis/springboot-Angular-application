import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TradingExeCommandInspectorComponent } from './trading-exe-command-inspector.component';

describe('TradingExeCommandInspectorComponent', () => {
  let component: TradingExeCommandInspectorComponent;
  let fixture: ComponentFixture<TradingExeCommandInspectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TradingExeCommandInspectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TradingExeCommandInspectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
