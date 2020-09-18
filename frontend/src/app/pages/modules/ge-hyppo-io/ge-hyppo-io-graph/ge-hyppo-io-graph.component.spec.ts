import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GeHyppoIoGraphComponent } from './ge-hyppo-io-graph.component';

describe('GeHyppoIoGraphComponent', () => {
  let component: GeHyppoIoGraphComponent;
  let fixture: ComponentFixture<GeHyppoIoGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GeHyppoIoGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GeHyppoIoGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
