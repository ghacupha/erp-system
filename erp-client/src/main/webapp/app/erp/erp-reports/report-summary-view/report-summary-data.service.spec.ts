import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ReportSummaryDataService } from './report-summary-data.service';

describe('ReportSummaryDataService', () => {
  let service: ReportSummaryDataService;
  let httpMock: HttpTestingController;
  let applicationConfigService: ApplicationConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApplicationConfigService],
    });

    service = TestBed.inject(ReportSummaryDataService);
    httpMock = TestBed.inject(HttpTestingController);
    applicationConfigService = TestBed.inject(ApplicationConfigService);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should resolve endpoint placeholders and omit path params from query string', () => {
    const endpoint = '/api/leases/interest-expense-summary/{leasePeriodId}';
    jest.spyOn(applicationConfigService, 'getEndpointFor').mockReturnValue(endpoint);

    service
      .fetchSummary('api/leases/interest-expense-summary/{leasePeriodId}', {
        leasePeriodId: 42,
        extra: 'value',
      })
      .subscribe();

    const req = httpMock.expectOne(request => request.url === '/api/leases/interest-expense-summary/42');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('leasePeriodId')).toBeNull();
    expect(req.request.params.get('extra')).toBe('value');
    expect(req.request.params.get('size')).toBe('200');

    req.flush([]);
  });
});
