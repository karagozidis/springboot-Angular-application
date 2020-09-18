import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveSubstationsMainComponent } from './active-substations-main.component';

describe('ActiveSubstationsMainComponent', () => {
  let component: ActiveSubstationsMainComponent;
  let fixture: ComponentFixture<ActiveSubstationsMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActiveSubstationsMainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveSubstationsMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
