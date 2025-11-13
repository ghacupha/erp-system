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

import { IFraudType, FraudType } from '../fraud-type.model';

import { FraudTypeService } from './fraud-type.service';

describe('FraudType Service', () => {
  let service: FraudTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFraudType;
  let expectedResult: IFraudType | IFraudType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FraudTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fraudTypeCode: 'AAAAAAA',
      fraudType: 'AAAAAAA',
      fraudTypeDetails: 'AAAAAAA',
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

    it('should create a FraudType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FraudType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FraudType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fraudTypeCode: 'BBBBBB',
          fraudType: 'BBBBBB',
          fraudTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FraudType', () => {
      const patchObject = Object.assign(
        {
          fraudTypeCode: 'BBBBBB',
        },
        new FraudType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FraudType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fraudTypeCode: 'BBBBBB',
          fraudType: 'BBBBBB',
          fraudTypeDetails: 'BBBBBB',
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

    it('should delete a FraudType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFraudTypeToCollectionIfMissing', () => {
      it('should add a FraudType to an empty array', () => {
        const fraudType: IFraudType = { id: 123 };
        expectedResult = service.addFraudTypeToCollectionIfMissing([], fraudType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fraudType);
      });

      it('should not add a FraudType to an array that contains it', () => {
        const fraudType: IFraudType = { id: 123 };
        const fraudTypeCollection: IFraudType[] = [
          {
            ...fraudType,
          },
          { id: 456 },
        ];
        expectedResult = service.addFraudTypeToCollectionIfMissing(fraudTypeCollection, fraudType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FraudType to an array that doesn't contain it", () => {
        const fraudType: IFraudType = { id: 123 };
        const fraudTypeCollection: IFraudType[] = [{ id: 456 }];
        expectedResult = service.addFraudTypeToCollectionIfMissing(fraudTypeCollection, fraudType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fraudType);
      });

      it('should add only unique FraudType to an array', () => {
        const fraudTypeArray: IFraudType[] = [{ id: 123 }, { id: 456 }, { id: 63131 }];
        const fraudTypeCollection: IFraudType[] = [{ id: 123 }];
        expectedResult = service.addFraudTypeToCollectionIfMissing(fraudTypeCollection, ...fraudTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fraudType: IFraudType = { id: 123 };
        const fraudType2: IFraudType = { id: 456 };
        expectedResult = service.addFraudTypeToCollectionIfMissing([], fraudType, fraudType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fraudType);
        expect(expectedResult).toContain(fraudType2);
      });

      it('should accept null and undefined values', () => {
        const fraudType: IFraudType = { id: 123 };
        expectedResult = service.addFraudTypeToCollectionIfMissing([], null, fraudType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fraudType);
      });

      it('should return initial array if no FraudType is added', () => {
        const fraudTypeCollection: IFraudType[] = [{ id: 123 }];
        expectedResult = service.addFraudTypeToCollectionIfMissing(fraudTypeCollection, undefined, null);
        expect(expectedResult).toEqual(fraudTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
