import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketNotificationMessagesComponent } from './market-notification-messages.component';

describe('MarketNotificationMessagesComponent', () => {
  let component: MarketNotificationMessagesComponent;
  let fixture: ComponentFixture<MarketNotificationMessagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketNotificationMessagesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketNotificationMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
