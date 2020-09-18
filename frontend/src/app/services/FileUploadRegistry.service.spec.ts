/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { FileUploadRegistryService } from './FileUploadRegistry.service';

describe('Service: FileUploadRegistry', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FileUploadRegistryService]
    });
  });

  it('should ...', inject([FileUploadRegistryService], (service: FileUploadRegistryService) => {
    expect(service).toBeTruthy();
  }));
});
