///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
