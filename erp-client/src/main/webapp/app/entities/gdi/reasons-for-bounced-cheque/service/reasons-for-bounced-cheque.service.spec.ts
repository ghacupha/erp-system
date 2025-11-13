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

import { IReasonsForBouncedCheque, ReasonsForBouncedCheque } from '../reasons-for-bounced-cheque.model';

import { ReasonsForBouncedChequeService } from './reasons-for-bounced-cheque.service';

describe('ReasonsForBouncedCheque Service', () => {
  let service: ReasonsForBouncedChequeService;
  let httpMock: HttpTestingController;
  let elemDefault: IReasonsForBouncedCheque;
  let expectedResult: IReasonsForBouncedCheque | IReasonsForBouncedCheque[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReasonsForBouncedChequeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bouncedChequeReasonsTypeCode: 'AAAAAAA',
      bouncedChequeReasonsType: 'AAAAAAA',
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

    it('should create a ReasonsForBouncedCheque', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ReasonsForBouncedCheque()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReasonsForBouncedCheque', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bouncedChequeReasonsTypeCode: 'BBBBBB',
          bouncedChequeReasonsType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReasonsForBouncedCheque', () => {
      const patchObject = Object.assign({}, new ReasonsForBouncedCheque());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReasonsForBouncedCheque', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bouncedChequeReasonsTypeCode: 'BBBBBB',
          bouncedChequeReasonsType: 'BBBBBB',
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

    it('should delete a ReasonsForBouncedCheque', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReasonsForBouncedChequeToCollectionIfMissing', () => {
      it('should add a ReasonsForBouncedCheque to an empty array', () => {
        const reasonsForBouncedCheque: IReasonsForBouncedCheque = { id: 123 };
        expectedResult = service.addReasonsForBouncedChequeToCollectionIfMissing([], reasonsForBouncedCheque);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reasonsForBouncedCheque);
      });

      it('should not add a ReasonsForBouncedCheque to an array that contains it', () => {
        const reasonsForBouncedCheque: IReasonsForBouncedCheque = { id: 123 };
        const reasonsForBouncedChequeCollection: IReasonsForBouncedCheque[] = [
          {
            ...reasonsForBouncedCheque,
          },
          { id: 456 },
        ];
        expectedResult = service.addReasonsForBouncedChequeToCollectionIfMissing(
          reasonsForBouncedChequeCollection,
          reasonsForBouncedCheque
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReasonsForBouncedCheque to an array that doesn't contain it", () => {
        const reasonsForBouncedCheque: IReasonsForBouncedCheque = { id: 123 };
        const reasonsForBouncedChequeCollection: IReasonsForBouncedCheque[] = [{ id: 456 }];
        expectedResult = service.addReasonsForBouncedChequeToCollectionIfMissing(
          reasonsForBouncedChequeCollection,
          reasonsForBouncedCheque
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reasonsForBouncedCheque);
      });

      it('should add only unique ReasonsForBouncedCheque to an array', () => {
        const reasonsForBouncedChequeArray: IReasonsForBouncedCheque[] = [{ id: 123 }, { id: 456 }, { id: 86112 }];
        const reasonsForBouncedChequeCollection: IReasonsForBouncedCheque[] = [{ id: 123 }];
        expectedResult = service.addReasonsForBouncedChequeToCollectionIfMissing(
          reasonsForBouncedChequeCollection,
          ...reasonsForBouncedChequeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reasonsForBouncedCheque: IReasonsForBouncedCheque = { id: 123 };
        const reasonsForBouncedCheque2: IReasonsForBouncedCheque = { id: 456 };
        expectedResult = service.addReasonsForBouncedChequeToCollectionIfMissing([], reasonsForBouncedCheque, reasonsForBouncedCheque2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reasonsForBouncedCheque);
        expect(expectedResult).toContain(reasonsForBouncedCheque2);
      });

      it('should accept null and undefined values', () => {
        const reasonsForBouncedCheque: IReasonsForBouncedCheque = { id: 123 };
        expectedResult = service.addReasonsForBouncedChequeToCollectionIfMissing([], null, reasonsForBouncedCheque, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reasonsForBouncedCheque);
      });

      it('should return initial array if no ReasonsForBouncedCheque is added', () => {
        const reasonsForBouncedChequeCollection: IReasonsForBouncedCheque[] = [{ id: 123 }];
        expectedResult = service.addReasonsForBouncedChequeToCollectionIfMissing(reasonsForBouncedChequeCollection, undefined, null);
        expect(expectedResult).toEqual(reasonsForBouncedChequeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
