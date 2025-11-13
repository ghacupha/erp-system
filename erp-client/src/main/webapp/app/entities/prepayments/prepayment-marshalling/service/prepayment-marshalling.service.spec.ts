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

import { IPrepaymentMarshalling, PrepaymentMarshalling } from '../prepayment-marshalling.model';

import { PrepaymentMarshallingService } from './prepayment-marshalling.service';

describe('PrepaymentMarshalling Service', () => {
  let service: PrepaymentMarshallingService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrepaymentMarshalling;
  let expectedResult: IPrepaymentMarshalling | IPrepaymentMarshalling[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrepaymentMarshallingService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      inactive: false,
      amortizationPeriods: 0,
      processed: false,
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

    it('should create a PrepaymentMarshalling', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PrepaymentMarshalling()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrepaymentMarshalling', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          inactive: true,
          amortizationPeriods: 1,
          processed: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrepaymentMarshalling', () => {
      const patchObject = Object.assign(
        {
          inactive: true,
        },
        new PrepaymentMarshalling()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrepaymentMarshalling', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          inactive: true,
          amortizationPeriods: 1,
          processed: true,
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

    it('should delete a PrepaymentMarshalling', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrepaymentMarshallingToCollectionIfMissing', () => {
      it('should add a PrepaymentMarshalling to an empty array', () => {
        const prepaymentMarshalling: IPrepaymentMarshalling = { id: 123 };
        expectedResult = service.addPrepaymentMarshallingToCollectionIfMissing([], prepaymentMarshalling);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentMarshalling);
      });

      it('should not add a PrepaymentMarshalling to an array that contains it', () => {
        const prepaymentMarshalling: IPrepaymentMarshalling = { id: 123 };
        const prepaymentMarshallingCollection: IPrepaymentMarshalling[] = [
          {
            ...prepaymentMarshalling,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrepaymentMarshallingToCollectionIfMissing(prepaymentMarshallingCollection, prepaymentMarshalling);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrepaymentMarshalling to an array that doesn't contain it", () => {
        const prepaymentMarshalling: IPrepaymentMarshalling = { id: 123 };
        const prepaymentMarshallingCollection: IPrepaymentMarshalling[] = [{ id: 456 }];
        expectedResult = service.addPrepaymentMarshallingToCollectionIfMissing(prepaymentMarshallingCollection, prepaymentMarshalling);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentMarshalling);
      });

      it('should add only unique PrepaymentMarshalling to an array', () => {
        const prepaymentMarshallingArray: IPrepaymentMarshalling[] = [{ id: 123 }, { id: 456 }, { id: 16651 }];
        const prepaymentMarshallingCollection: IPrepaymentMarshalling[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentMarshallingToCollectionIfMissing(
          prepaymentMarshallingCollection,
          ...prepaymentMarshallingArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prepaymentMarshalling: IPrepaymentMarshalling = { id: 123 };
        const prepaymentMarshalling2: IPrepaymentMarshalling = { id: 456 };
        expectedResult = service.addPrepaymentMarshallingToCollectionIfMissing([], prepaymentMarshalling, prepaymentMarshalling2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prepaymentMarshalling);
        expect(expectedResult).toContain(prepaymentMarshalling2);
      });

      it('should accept null and undefined values', () => {
        const prepaymentMarshalling: IPrepaymentMarshalling = { id: 123 };
        expectedResult = service.addPrepaymentMarshallingToCollectionIfMissing([], null, prepaymentMarshalling, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prepaymentMarshalling);
      });

      it('should return initial array if no PrepaymentMarshalling is added', () => {
        const prepaymentMarshallingCollection: IPrepaymentMarshalling[] = [{ id: 123 }];
        expectedResult = service.addPrepaymentMarshallingToCollectionIfMissing(prepaymentMarshallingCollection, undefined, null);
        expect(expectedResult).toEqual(prepaymentMarshallingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
