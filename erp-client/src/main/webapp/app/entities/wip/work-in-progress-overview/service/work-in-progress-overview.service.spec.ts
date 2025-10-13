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

import { IWorkInProgressOverview } from '../work-in-progress-overview.model';

import { WorkInProgressOverviewService } from './work-in-progress-overview.service';

describe('WorkInProgressOverview Service', () => {
  let service: WorkInProgressOverviewService;
  let httpMock: HttpTestingController;
  let elemDefault: IWorkInProgressOverview;
  let expectedResult: IWorkInProgressOverview | IWorkInProgressOverview[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkInProgressOverviewService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      numberOfItems: 0,
      instalmentAmount: 0,
      totalTransferAmount: 0,
      outstandingAmount: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should return a list of WorkInProgressOverview', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numberOfItems: 1,
          instalmentAmount: 1,
          totalTransferAmount: 1,
          outstandingAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    describe('addWorkInProgressOverviewToCollectionIfMissing', () => {
      it('should add a WorkInProgressOverview to an empty array', () => {
        const workInProgressOverview: IWorkInProgressOverview = { id: 123 };
        expectedResult = service.addWorkInProgressOverviewToCollectionIfMissing([], workInProgressOverview);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressOverview);
      });

      it('should not add a WorkInProgressOverview to an array that contains it', () => {
        const workInProgressOverview: IWorkInProgressOverview = { id: 123 };
        const workInProgressOverviewCollection: IWorkInProgressOverview[] = [
          {
            ...workInProgressOverview,
          },
          { id: 456 },
        ];
        expectedResult = service.addWorkInProgressOverviewToCollectionIfMissing(workInProgressOverviewCollection, workInProgressOverview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkInProgressOverview to an array that doesn't contain it", () => {
        const workInProgressOverview: IWorkInProgressOverview = { id: 123 };
        const workInProgressOverviewCollection: IWorkInProgressOverview[] = [{ id: 456 }];
        expectedResult = service.addWorkInProgressOverviewToCollectionIfMissing(workInProgressOverviewCollection, workInProgressOverview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressOverview);
      });

      it('should add only unique WorkInProgressOverview to an array', () => {
        const workInProgressOverviewArray: IWorkInProgressOverview[] = [{ id: 123 }, { id: 456 }, { id: 4875 }];
        const workInProgressOverviewCollection: IWorkInProgressOverview[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressOverviewToCollectionIfMissing(
          workInProgressOverviewCollection,
          ...workInProgressOverviewArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workInProgressOverview: IWorkInProgressOverview = { id: 123 };
        const workInProgressOverview2: IWorkInProgressOverview = { id: 456 };
        expectedResult = service.addWorkInProgressOverviewToCollectionIfMissing([], workInProgressOverview, workInProgressOverview2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressOverview);
        expect(expectedResult).toContain(workInProgressOverview2);
      });

      it('should accept null and undefined values', () => {
        const workInProgressOverview: IWorkInProgressOverview = { id: 123 };
        expectedResult = service.addWorkInProgressOverviewToCollectionIfMissing([], null, workInProgressOverview, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressOverview);
      });

      it('should return initial array if no WorkInProgressOverview is added', () => {
        const workInProgressOverviewCollection: IWorkInProgressOverview[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressOverviewToCollectionIfMissing(workInProgressOverviewCollection, undefined, null);
        expect(expectedResult).toEqual(workInProgressOverviewCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
