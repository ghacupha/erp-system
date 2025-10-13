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

import { IIsoCurrencyCode, IsoCurrencyCode } from '../iso-currency-code.model';

import { IsoCurrencyCodeService } from './iso-currency-code.service';

describe('IsoCurrencyCode Service', () => {
  let service: IsoCurrencyCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IIsoCurrencyCode;
  let expectedResult: IIsoCurrencyCode | IIsoCurrencyCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IsoCurrencyCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      alphabeticCode: 'AAAAAAA',
      numericCode: 'AAAAAAA',
      minorUnit: 'AAAAAAA',
      currency: 'AAAAAAA',
      country: 'AAAAAAA',
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

    it('should create a IsoCurrencyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new IsoCurrencyCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IsoCurrencyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          alphabeticCode: 'BBBBBB',
          numericCode: 'BBBBBB',
          minorUnit: 'BBBBBB',
          currency: 'BBBBBB',
          country: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IsoCurrencyCode', () => {
      const patchObject = Object.assign(
        {
          numericCode: 'BBBBBB',
          minorUnit: 'BBBBBB',
        },
        new IsoCurrencyCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IsoCurrencyCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          alphabeticCode: 'BBBBBB',
          numericCode: 'BBBBBB',
          minorUnit: 'BBBBBB',
          currency: 'BBBBBB',
          country: 'BBBBBB',
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

    it('should delete a IsoCurrencyCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIsoCurrencyCodeToCollectionIfMissing', () => {
      it('should add a IsoCurrencyCode to an empty array', () => {
        const isoCurrencyCode: IIsoCurrencyCode = { id: 123 };
        expectedResult = service.addIsoCurrencyCodeToCollectionIfMissing([], isoCurrencyCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isoCurrencyCode);
      });

      it('should not add a IsoCurrencyCode to an array that contains it', () => {
        const isoCurrencyCode: IIsoCurrencyCode = { id: 123 };
        const isoCurrencyCodeCollection: IIsoCurrencyCode[] = [
          {
            ...isoCurrencyCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addIsoCurrencyCodeToCollectionIfMissing(isoCurrencyCodeCollection, isoCurrencyCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IsoCurrencyCode to an array that doesn't contain it", () => {
        const isoCurrencyCode: IIsoCurrencyCode = { id: 123 };
        const isoCurrencyCodeCollection: IIsoCurrencyCode[] = [{ id: 456 }];
        expectedResult = service.addIsoCurrencyCodeToCollectionIfMissing(isoCurrencyCodeCollection, isoCurrencyCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isoCurrencyCode);
      });

      it('should add only unique IsoCurrencyCode to an array', () => {
        const isoCurrencyCodeArray: IIsoCurrencyCode[] = [{ id: 123 }, { id: 456 }, { id: 55293 }];
        const isoCurrencyCodeCollection: IIsoCurrencyCode[] = [{ id: 123 }];
        expectedResult = service.addIsoCurrencyCodeToCollectionIfMissing(isoCurrencyCodeCollection, ...isoCurrencyCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const isoCurrencyCode: IIsoCurrencyCode = { id: 123 };
        const isoCurrencyCode2: IIsoCurrencyCode = { id: 456 };
        expectedResult = service.addIsoCurrencyCodeToCollectionIfMissing([], isoCurrencyCode, isoCurrencyCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isoCurrencyCode);
        expect(expectedResult).toContain(isoCurrencyCode2);
      });

      it('should accept null and undefined values', () => {
        const isoCurrencyCode: IIsoCurrencyCode = { id: 123 };
        expectedResult = service.addIsoCurrencyCodeToCollectionIfMissing([], null, isoCurrencyCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isoCurrencyCode);
      });

      it('should return initial array if no IsoCurrencyCode is added', () => {
        const isoCurrencyCodeCollection: IIsoCurrencyCode[] = [{ id: 123 }];
        expectedResult = service.addIsoCurrencyCodeToCollectionIfMissing(isoCurrencyCodeCollection, undefined, null);
        expect(expectedResult).toEqual(isoCurrencyCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
