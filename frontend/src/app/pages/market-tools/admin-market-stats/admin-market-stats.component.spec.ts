import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminMarketStatsComponent } from './admin-market-stats.component';

describe('AdminMarketStatsComponent', () => {
  let component: AdminMarketStatsComponent;
  let fixture: ComponentFixture<AdminMarketStatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminMarketStatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminMarketStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
