import { TestBed } from '@angular/core/testing';

import { MarketScenarioService } from './market-scenario.service';

describe('MarketScenarioService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MarketScenarioService = TestBed.get(MarketScenarioService);
    expect(service).toBeTruthy();
  });
});
