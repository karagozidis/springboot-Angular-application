import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductTransactionsComponent } from './product-transactions.component';

describe('ProductTransactionsComponent', () => {
  let component: ProductTransactionsComponent;
  let fixture: ComponentFixture<ProductTransactionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductTransactionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductTransactionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
