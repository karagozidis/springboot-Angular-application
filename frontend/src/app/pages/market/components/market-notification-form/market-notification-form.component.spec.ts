import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketNotificationFormComponent } from './market-notification-form.component';

describe('MarketNotificationFormComponent', () => {
  let component: MarketNotificationFormComponent;
  let fixture: ComponentFixture<MarketNotificationFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketNotificationFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketNotificationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
