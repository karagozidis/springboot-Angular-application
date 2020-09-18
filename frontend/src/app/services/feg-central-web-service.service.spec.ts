/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CrmCentralWebSocketsService } from './crm-central-web-sockets.service';

describe('Service: CrmCentralWebService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CrmCentralWebSocketsService]
    });
  });

  it('should ...', inject([CrmCentralWebSocketsService], (service: CrmCentralWebSocketsService) => {
    expect(service).toBeTruthy();
  }));
});
