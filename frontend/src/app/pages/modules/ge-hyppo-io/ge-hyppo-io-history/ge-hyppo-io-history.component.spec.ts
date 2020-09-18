import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GeHyppoIoHistoryComponent } from './ge-hyppo-io-history.component';

describe('GeHyppoIoHistoryComponent', () => {
  let component: GeHyppoIoHistoryComponent;
  let fixture: ComponentFixture<GeHyppoIoHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GeHyppoIoHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GeHyppoIoHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
