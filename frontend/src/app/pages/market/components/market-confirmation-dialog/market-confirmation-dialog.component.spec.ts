import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketConfirmationDialogComponent } from './market-confirmation-dialog.component';

describe('MarketConfirmationDialogComponent', () => {
  let component: MarketConfirmationDialogComponent;
  let fixture: ComponentFixture<MarketConfirmationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketConfirmationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketConfirmationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
