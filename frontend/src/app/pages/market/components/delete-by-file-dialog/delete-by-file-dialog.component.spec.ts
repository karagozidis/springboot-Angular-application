import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteByFileDialogComponent } from './delete-by-file-dialog.component';

describe('DeleteByFileDialogComponent', () => {
  let component: DeleteByFileDialogComponent;
  let fixture: ComponentFixture<DeleteByFileDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteByFileDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteByFileDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
