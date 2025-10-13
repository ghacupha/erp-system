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

import { ILegalStatus, LegalStatus } from '../legal-status.model';

import { LegalStatusService } from './legal-status.service';

describe('LegalStatus Service', () => {
  let service: LegalStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: ILegalStatus;
  let expectedResult: ILegalStatus | ILegalStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LegalStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      legalStatusCode: 'AAAAAAA',
      legalStatusType: 'AAAAAAA',
      legalStatusDescription: 'AAAAAAA',
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

    it('should create a LegalStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LegalStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LegalStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          legalStatusCode: 'BBBBBB',
          legalStatusType: 'BBBBBB',
          legalStatusDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LegalStatus', () => {
      const patchObject = Object.assign(
        {
          legalStatusCode: 'BBBBBB',
        },
        new LegalStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LegalStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          legalStatusCode: 'BBBBBB',
          legalStatusType: 'BBBBBB',
          legalStatusDescription: 'BBBBBB',
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

    it('should delete a LegalStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLegalStatusToCollectionIfMissing', () => {
      it('should add a LegalStatus to an empty array', () => {
        const legalStatus: ILegalStatus = { id: 123 };
        expectedResult = service.addLegalStatusToCollectionIfMissing([], legalStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(legalStatus);
      });

      it('should not add a LegalStatus to an array that contains it', () => {
        const legalStatus: ILegalStatus = { id: 123 };
        const legalStatusCollection: ILegalStatus[] = [
          {
            ...legalStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addLegalStatusToCollectionIfMissing(legalStatusCollection, legalStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LegalStatus to an array that doesn't contain it", () => {
        const legalStatus: ILegalStatus = { id: 123 };
        const legalStatusCollection: ILegalStatus[] = [{ id: 456 }];
        expectedResult = service.addLegalStatusToCollectionIfMissing(legalStatusCollection, legalStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(legalStatus);
      });

      it('should add only unique LegalStatus to an array', () => {
        const legalStatusArray: ILegalStatus[] = [{ id: 123 }, { id: 456 }, { id: 8641 }];
        const legalStatusCollection: ILegalStatus[] = [{ id: 123 }];
        expectedResult = service.addLegalStatusToCollectionIfMissing(legalStatusCollection, ...legalStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const legalStatus: ILegalStatus = { id: 123 };
        const legalStatus2: ILegalStatus = { id: 456 };
        expectedResult = service.addLegalStatusToCollectionIfMissing([], legalStatus, legalStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(legalStatus);
        expect(expectedResult).toContain(legalStatus2);
      });

      it('should accept null and undefined values', () => {
        const legalStatus: ILegalStatus = { id: 123 };
        expectedResult = service.addLegalStatusToCollectionIfMissing([], null, legalStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(legalStatus);
      });

      it('should return initial array if no LegalStatus is added', () => {
        const legalStatusCollection: ILegalStatus[] = [{ id: 123 }];
        expectedResult = service.addLegalStatusToCollectionIfMissing(legalStatusCollection, undefined, null);
        expect(expectedResult).toEqual(legalStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
