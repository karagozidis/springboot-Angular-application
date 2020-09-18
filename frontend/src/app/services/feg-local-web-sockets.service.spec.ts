/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CrmLocalWebSocketsService } from './crm-local-web-sockets.service';

describe('Service: CrmLocalWebSockets', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CrmLocalWebSocketsService]
    });
  });

  it('should ...', inject([CrmLocalWebSocketsService], (service: CrmLocalWebSocketsService) => {
    expect(service).toBeTruthy();
  }));
});
