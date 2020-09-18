import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProductTransactionsComponent } from './user-product-transactions.component';

describe('UserProductTransactionsComponent', () => {
  let component: UserProductTransactionsComponent;
  let fixture: ComponentFixture<UserProductTransactionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserProductTransactionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserProductTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
