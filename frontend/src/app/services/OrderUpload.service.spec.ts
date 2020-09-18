/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrderUploadService } from './OrderUpload.service';

describe('Service: OrderUpload', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrderUploadService]
    });
  });

  it('should ...', inject([OrderUploadService], (service: OrderUploadService) => {
    expect(service).toBeTruthy();
  }));
});
