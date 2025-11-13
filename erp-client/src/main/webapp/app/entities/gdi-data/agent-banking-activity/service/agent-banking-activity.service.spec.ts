///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAgentBankingActivity, AgentBankingActivity } from '../agent-banking-activity.model';

import { AgentBankingActivityService } from './agent-banking-activity.service';

describe('AgentBankingActivity Service', () => {
  let service: AgentBankingActivityService;
  let httpMock: HttpTestingController;
  let elemDefault: IAgentBankingActivity;
  let expectedResult: IAgentBankingActivity | IAgentBankingActivity[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgentBankingActivityService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      agentUniqueId: 'AAAAAAA',
      terminalUniqueId: 'AAAAAAA',
      totalCountOfTransactions: 0,
      totalValueOfTransactionsInLCY: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AgentBankingActivity', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AgentBankingActivity()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgentBankingActivity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          agentUniqueId: 'BBBBBB',
          terminalUniqueId: 'BBBBBB',
          totalCountOfTransactions: 1,
          totalValueOfTransactionsInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AgentBankingActivity', () => {
      const patchObject = Object.assign(
        {
          terminalUniqueId: 'BBBBBB',
          totalValueOfTransactionsInLCY: 1,
        },
        new AgentBankingActivity()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AgentBankingActivity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          agentUniqueId: 'BBBBBB',
          terminalUniqueId: 'BBBBBB',
          totalCountOfTransactions: 1,
          totalValueOfTransactionsInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AgentBankingActivity', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAgentBankingActivityToCollectionIfMissing', () => {
      it('should add a AgentBankingActivity to an empty array', () => {
        const agentBankingActivity: IAgentBankingActivity = { id: 123 };
        expectedResult = service.addAgentBankingActivityToCollectionIfMissing([], agentBankingActivity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agentBankingActivity);
      });

      it('should not add a AgentBankingActivity to an array that contains it', () => {
        const agentBankingActivity: IAgentBankingActivity = { id: 123 };
        const agentBankingActivityCollection: IAgentBankingActivity[] = [
          {
            ...agentBankingActivity,
          },
          { id: 456 },
        ];
        expectedResult = service.addAgentBankingActivityToCollectionIfMissing(agentBankingActivityCollection, agentBankingActivity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgentBankingActivity to an array that doesn't contain it", () => {
        const agentBankingActivity: IAgentBankingActivity = { id: 123 };
        const agentBankingActivityCollection: IAgentBankingActivity[] = [{ id: 456 }];
        expectedResult = service.addAgentBankingActivityToCollectionIfMissing(agentBankingActivityCollection, agentBankingActivity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agentBankingActivity);
      });

      it('should add only unique AgentBankingActivity to an array', () => {
        const agentBankingActivityArray: IAgentBankingActivity[] = [{ id: 123 }, { id: 456 }, { id: 36903 }];
        const agentBankingActivityCollection: IAgentBankingActivity[] = [{ id: 123 }];
        expectedResult = service.addAgentBankingActivityToCollectionIfMissing(agentBankingActivityCollection, ...agentBankingActivityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agentBankingActivity: IAgentBankingActivity = { id: 123 };
        const agentBankingActivity2: IAgentBankingActivity = { id: 456 };
        expectedResult = service.addAgentBankingActivityToCollectionIfMissing([], agentBankingActivity, agentBankingActivity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agentBankingActivity);
        expect(expectedResult).toContain(agentBankingActivity2);
      });

      it('should accept null and undefined values', () => {
        const agentBankingActivity: IAgentBankingActivity = { id: 123 };
        expectedResult = service.addAgentBankingActivityToCollectionIfMissing([], null, agentBankingActivity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agentBankingActivity);
      });

      it('should return initial array if no AgentBankingActivity is added', () => {
        const agentBankingActivityCollection: IAgentBankingActivity[] = [{ id: 123 }];
        expectedResult = service.addAgentBankingActivityToCollectionIfMissing(agentBankingActivityCollection, undefined, null);
        expect(expectedResult).toEqual(agentBankingActivityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
