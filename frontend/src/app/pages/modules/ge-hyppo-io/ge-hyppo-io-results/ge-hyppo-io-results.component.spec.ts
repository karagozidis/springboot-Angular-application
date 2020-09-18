import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GeHyppoIoResultsComponent } from './ge-hyppo-io-results.component';

describe('GeHyppoIoResultsComponent', () => {
  let component: GeHyppoIoResultsComponent;
  let fixture: ComponentFixture<GeHyppoIoResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GeHyppoIoResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GeHyppoIoResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
