/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrderUploadRegistryServiceService } from './OrderUploadRegistryService.service';

describe('Service: OrderUploadRegistryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrderUploadRegistryServiceService]
    });
  });

  it('should ...', inject([OrderUploadRegistryServiceService], (service: OrderUploadRegistryServiceService) => {
    expect(service).toBeTruthy();
  }));
});
