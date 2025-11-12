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

import { MetricsService } from './metrics.service';
import { ThreadDump, ThreadState } from './metrics.model';

describe('Logs Service', () => {
  let service: MetricsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(MetricsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('Service methods', () => {
    it('should return Metrics', () => {
      let expectedResult;
      const metrics = {
        jvm: {},
        'http.server.requests': {},
        cache: {},
        services: {},
        databases: {},
        garbageCollector: {},
        processMetrics: {},
      };

      service.getMetrics().subscribe(received => {
        expectedResult = received;
      });

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(metrics);
      expect(expectedResult).toEqual(metrics);
    });

    it('should return Thread Dump', () => {
      let expectedResult: ThreadDump | null = null;
      const dump: ThreadDump = {
        threads: [
          {
            threadName: 'Reference Handler',
            threadId: 2,
            blockedTime: -1,
            blockedCount: 7,
            waitedTime: -1,
            waitedCount: 0,
            lockName: null,
            lockOwnerId: -1,
            lockOwnerName: null,
            daemon: true,
            inNative: false,
            suspended: false,
            threadState: ThreadState.Runnable,
            priority: 10,
            stackTrace: [],
            lockedMonitors: [],
            lockedSynchronizers: [],
            lockInfo: null,
          },
        ],
      };

      service.threadDump().subscribe(received => {
        expectedResult = received;
      });

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(dump);
      expect(expectedResult).toEqual(dump);
    });
  });
});
