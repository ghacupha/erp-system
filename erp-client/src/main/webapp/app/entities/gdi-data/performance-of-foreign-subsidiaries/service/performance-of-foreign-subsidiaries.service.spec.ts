///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { IPerformanceOfForeignSubsidiaries, PerformanceOfForeignSubsidiaries } from '../performance-of-foreign-subsidiaries.model';

import { PerformanceOfForeignSubsidiariesService } from './performance-of-foreign-subsidiaries.service';

describe('PerformanceOfForeignSubsidiaries Service', () => {
  let service: PerformanceOfForeignSubsidiariesService;
  let httpMock: HttpTestingController;
  let elemDefault: IPerformanceOfForeignSubsidiaries;
  let expectedResult: IPerformanceOfForeignSubsidiaries | IPerformanceOfForeignSubsidiaries[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PerformanceOfForeignSubsidiariesService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      subsidiaryName: 'AAAAAAA',
      reportingDate: currentDate,
      subsidiaryId: 'AAAAAAA',
      grossLoansAmount: 0,
      grossNPALoanAmount: 0,
      grossAssetsAmount: 0,
      grossDepositsAmount: 0,
      profitBeforeTax: 0,
      totalCapitalAdequacyRatio: 0,
      liquidityRatio: 0,
      generalProvisions: 0,
      specificProvisions: 0,
      interestInSuspenseAmount: 0,
      totalNumberOfStaff: 0,
      numberOfBranches: 0,
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

    it('should create a PerformanceOfForeignSubsidiaries', () => {
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

      service.create(new PerformanceOfForeignSubsidiaries()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PerformanceOfForeignSubsidiaries', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subsidiaryName: 'BBBBBB',
          reportingDate: currentDate.format(DATE_FORMAT),
          subsidiaryId: 'BBBBBB',
          grossLoansAmount: 1,
          grossNPALoanAmount: 1,
          grossAssetsAmount: 1,
          grossDepositsAmount: 1,
          profitBeforeTax: 1,
          totalCapitalAdequacyRatio: 1,
          liquidityRatio: 1,
          generalProvisions: 1,
          specificProvisions: 1,
          interestInSuspenseAmount: 1,
          totalNumberOfStaff: 1,
          numberOfBranches: 1,
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

    it('should partial update a PerformanceOfForeignSubsidiaries', () => {
      const patchObject = Object.assign(
        {
          grossAssetsAmount: 1,
          grossDepositsAmount: 1,
          profitBeforeTax: 1,
          totalCapitalAdequacyRatio: 1,
          specificProvisions: 1,
          interestInSuspenseAmount: 1,
        },
        new PerformanceOfForeignSubsidiaries()
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

    it('should return a list of PerformanceOfForeignSubsidiaries', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          subsidiaryName: 'BBBBBB',
          reportingDate: currentDate.format(DATE_FORMAT),
          subsidiaryId: 'BBBBBB',
          grossLoansAmount: 1,
          grossNPALoanAmount: 1,
          grossAssetsAmount: 1,
          grossDepositsAmount: 1,
          profitBeforeTax: 1,
          totalCapitalAdequacyRatio: 1,
          liquidityRatio: 1,
          generalProvisions: 1,
          specificProvisions: 1,
          interestInSuspenseAmount: 1,
          totalNumberOfStaff: 1,
          numberOfBranches: 1,
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

    it('should delete a PerformanceOfForeignSubsidiaries', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPerformanceOfForeignSubsidiariesToCollectionIfMissing', () => {
      it('should add a PerformanceOfForeignSubsidiaries to an empty array', () => {
        const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 123 };
        expectedResult = service.addPerformanceOfForeignSubsidiariesToCollectionIfMissing([], performanceOfForeignSubsidiaries);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(performanceOfForeignSubsidiaries);
      });

      it('should not add a PerformanceOfForeignSubsidiaries to an array that contains it', () => {
        const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 123 };
        const performanceOfForeignSubsidiariesCollection: IPerformanceOfForeignSubsidiaries[] = [
          {
            ...performanceOfForeignSubsidiaries,
          },
          { id: 456 },
        ];
        expectedResult = service.addPerformanceOfForeignSubsidiariesToCollectionIfMissing(
          performanceOfForeignSubsidiariesCollection,
          performanceOfForeignSubsidiaries
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PerformanceOfForeignSubsidiaries to an array that doesn't contain it", () => {
        const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 123 };
        const performanceOfForeignSubsidiariesCollection: IPerformanceOfForeignSubsidiaries[] = [{ id: 456 }];
        expectedResult = service.addPerformanceOfForeignSubsidiariesToCollectionIfMissing(
          performanceOfForeignSubsidiariesCollection,
          performanceOfForeignSubsidiaries
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(performanceOfForeignSubsidiaries);
      });

      it('should add only unique PerformanceOfForeignSubsidiaries to an array', () => {
        const performanceOfForeignSubsidiariesArray: IPerformanceOfForeignSubsidiaries[] = [{ id: 123 }, { id: 456 }, { id: 6227 }];
        const performanceOfForeignSubsidiariesCollection: IPerformanceOfForeignSubsidiaries[] = [{ id: 123 }];
        expectedResult = service.addPerformanceOfForeignSubsidiariesToCollectionIfMissing(
          performanceOfForeignSubsidiariesCollection,
          ...performanceOfForeignSubsidiariesArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 123 };
        const performanceOfForeignSubsidiaries2: IPerformanceOfForeignSubsidiaries = { id: 456 };
        expectedResult = service.addPerformanceOfForeignSubsidiariesToCollectionIfMissing(
          [],
          performanceOfForeignSubsidiaries,
          performanceOfForeignSubsidiaries2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(performanceOfForeignSubsidiaries);
        expect(expectedResult).toContain(performanceOfForeignSubsidiaries2);
      });

      it('should accept null and undefined values', () => {
        const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries = { id: 123 };
        expectedResult = service.addPerformanceOfForeignSubsidiariesToCollectionIfMissing(
          [],
          null,
          performanceOfForeignSubsidiaries,
          undefined
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(performanceOfForeignSubsidiaries);
      });

      it('should return initial array if no PerformanceOfForeignSubsidiaries is added', () => {
        const performanceOfForeignSubsidiariesCollection: IPerformanceOfForeignSubsidiaries[] = [{ id: 123 }];
        expectedResult = service.addPerformanceOfForeignSubsidiariesToCollectionIfMissing(
          performanceOfForeignSubsidiariesCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(performanceOfForeignSubsidiariesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
