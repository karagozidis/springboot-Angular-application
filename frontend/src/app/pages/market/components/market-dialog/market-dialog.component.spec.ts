import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketDialogComponent } from './market-dialog.component';

describe('MarketDialogComponent', () => {
  let component: MarketDialogComponent;
  let fixture: ComponentFixture<MarketDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
