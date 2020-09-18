import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneralNotificationFormComponent } from './general-notification-form.component';

describe('GeneralNotificationFormComponent', () => {
  let component: GeneralNotificationFormComponent;
  let fixture: ComponentFixture<GeneralNotificationFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GeneralNotificationFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GeneralNotificationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
