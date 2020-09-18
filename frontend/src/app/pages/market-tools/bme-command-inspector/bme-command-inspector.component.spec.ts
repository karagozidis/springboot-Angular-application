import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BmeCommandInspectorComponent } from './bme-command-inspector.component';

describe('BmeCommandInspectorComponent', () => {
  let component: BmeCommandInspectorComponent;
  let fixture: ComponentFixture<BmeCommandInspectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BmeCommandInspectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BmeCommandInspectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
