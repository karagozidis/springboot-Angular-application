import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderGeneratorComponent } from './order-generator.component';

describe('OrderGeneratorComponent', () => {
  let component: OrderGeneratorComponent;
  let fixture: ComponentFixture<OrderGeneratorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrderGeneratorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
