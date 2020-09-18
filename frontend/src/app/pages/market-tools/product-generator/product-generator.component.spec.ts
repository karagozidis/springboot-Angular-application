import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductGeneratorComponent } from './product-generator.component';

describe('ProductGeneratorComponent', () => {
  let component: ProductGeneratorComponent;
  let fixture: ComponentFixture<ProductGeneratorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductGeneratorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
