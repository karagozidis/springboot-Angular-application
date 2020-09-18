import { TestBed } from '@angular/core/testing';

import { InternalMessageService } from './internal-message.service';

describe('MessageServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: InternalMessageService = TestBed.get(InternalMessageService);
    expect(service).toBeTruthy();
  });
});
