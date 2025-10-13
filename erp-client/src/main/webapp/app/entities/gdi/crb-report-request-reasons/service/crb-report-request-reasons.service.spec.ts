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

import { ICrbReportRequestReasons, CrbReportRequestReasons } from '../crb-report-request-reasons.model';

import { CrbReportRequestReasonsService } from './crb-report-request-reasons.service';

describe('CrbReportRequestReasons Service', () => {
  let service: CrbReportRequestReasonsService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbReportRequestReasons;
  let expectedResult: ICrbReportRequestReasons | ICrbReportRequestReasons[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbReportRequestReasonsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      creditReportRequestReasonTypeCode: 'AAAAAAA',
      creditReportRequestReasonType: 'AAAAAAA',
      creditReportRequestDetails: 'AAAAAAA',
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

    it('should create a CrbReportRequestReasons', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbReportRequestReasons()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbReportRequestReasons', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditReportRequestReasonTypeCode: 'BBBBBB',
          creditReportRequestReasonType: 'BBBBBB',
          creditReportRequestDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbReportRequestReasons', () => {
      const patchObject = Object.assign(
        {
          creditReportRequestDetails: 'BBBBBB',
        },
        new CrbReportRequestReasons()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbReportRequestReasons', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creditReportRequestReasonTypeCode: 'BBBBBB',
          creditReportRequestReasonType: 'BBBBBB',
          creditReportRequestDetails: 'BBBBBB',
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

    it('should delete a CrbReportRequestReasons', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbReportRequestReasonsToCollectionIfMissing', () => {
      it('should add a CrbReportRequestReasons to an empty array', () => {
        const crbReportRequestReasons: ICrbReportRequestReasons = { id: 123 };
        expectedResult = service.addCrbReportRequestReasonsToCollectionIfMissing([], crbReportRequestReasons);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbReportRequestReasons);
      });

      it('should not add a CrbReportRequestReasons to an array that contains it', () => {
        const crbReportRequestReasons: ICrbReportRequestReasons = { id: 123 };
        const crbReportRequestReasonsCollection: ICrbReportRequestReasons[] = [
          {
            ...crbReportRequestReasons,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbReportRequestReasonsToCollectionIfMissing(
          crbReportRequestReasonsCollection,
          crbReportRequestReasons
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbReportRequestReasons to an array that doesn't contain it", () => {
        const crbReportRequestReasons: ICrbReportRequestReasons = { id: 123 };
        const crbReportRequestReasonsCollection: ICrbReportRequestReasons[] = [{ id: 456 }];
        expectedResult = service.addCrbReportRequestReasonsToCollectionIfMissing(
          crbReportRequestReasonsCollection,
          crbReportRequestReasons
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbReportRequestReasons);
      });

      it('should add only unique CrbReportRequestReasons to an array', () => {
        const crbReportRequestReasonsArray: ICrbReportRequestReasons[] = [{ id: 123 }, { id: 456 }, { id: 23989 }];
        const crbReportRequestReasonsCollection: ICrbReportRequestReasons[] = [{ id: 123 }];
        expectedResult = service.addCrbReportRequestReasonsToCollectionIfMissing(
          crbReportRequestReasonsCollection,
          ...crbReportRequestReasonsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbReportRequestReasons: ICrbReportRequestReasons = { id: 123 };
        const crbReportRequestReasons2: ICrbReportRequestReasons = { id: 456 };
        expectedResult = service.addCrbReportRequestReasonsToCollectionIfMissing([], crbReportRequestReasons, crbReportRequestReasons2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbReportRequestReasons);
        expect(expectedResult).toContain(crbReportRequestReasons2);
      });

      it('should accept null and undefined values', () => {
        const crbReportRequestReasons: ICrbReportRequestReasons = { id: 123 };
        expectedResult = service.addCrbReportRequestReasonsToCollectionIfMissing([], null, crbReportRequestReasons, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbReportRequestReasons);
      });

      it('should return initial array if no CrbReportRequestReasons is added', () => {
        const crbReportRequestReasonsCollection: ICrbReportRequestReasons[] = [{ id: 123 }];
        expectedResult = service.addCrbReportRequestReasonsToCollectionIfMissing(crbReportRequestReasonsCollection, undefined, null);
        expect(expectedResult).toEqual(crbReportRequestReasonsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
