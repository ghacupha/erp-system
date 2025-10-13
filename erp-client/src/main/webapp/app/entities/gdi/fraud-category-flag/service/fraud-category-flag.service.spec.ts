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

import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';
import { IFraudCategoryFlag, FraudCategoryFlag } from '../fraud-category-flag.model';

import { FraudCategoryFlagService } from './fraud-category-flag.service';

describe('FraudCategoryFlag Service', () => {
  let service: FraudCategoryFlagService;
  let httpMock: HttpTestingController;
  let elemDefault: IFraudCategoryFlag;
  let expectedResult: IFraudCategoryFlag | IFraudCategoryFlag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FraudCategoryFlagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fraudCategoryFlag: FlagCodes.Y,
      fraudCategoryTypeDetails: 'AAAAAAA',
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

    it('should create a FraudCategoryFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FraudCategoryFlag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FraudCategoryFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fraudCategoryFlag: 'BBBBBB',
          fraudCategoryTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FraudCategoryFlag', () => {
      const patchObject = Object.assign(
        {
          fraudCategoryFlag: 'BBBBBB',
          fraudCategoryTypeDetails: 'BBBBBB',
        },
        new FraudCategoryFlag()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FraudCategoryFlag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fraudCategoryFlag: 'BBBBBB',
          fraudCategoryTypeDetails: 'BBBBBB',
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

    it('should delete a FraudCategoryFlag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFraudCategoryFlagToCollectionIfMissing', () => {
      it('should add a FraudCategoryFlag to an empty array', () => {
        const fraudCategoryFlag: IFraudCategoryFlag = { id: 123 };
        expectedResult = service.addFraudCategoryFlagToCollectionIfMissing([], fraudCategoryFlag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fraudCategoryFlag);
      });

      it('should not add a FraudCategoryFlag to an array that contains it', () => {
        const fraudCategoryFlag: IFraudCategoryFlag = { id: 123 };
        const fraudCategoryFlagCollection: IFraudCategoryFlag[] = [
          {
            ...fraudCategoryFlag,
          },
          { id: 456 },
        ];
        expectedResult = service.addFraudCategoryFlagToCollectionIfMissing(fraudCategoryFlagCollection, fraudCategoryFlag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FraudCategoryFlag to an array that doesn't contain it", () => {
        const fraudCategoryFlag: IFraudCategoryFlag = { id: 123 };
        const fraudCategoryFlagCollection: IFraudCategoryFlag[] = [{ id: 456 }];
        expectedResult = service.addFraudCategoryFlagToCollectionIfMissing(fraudCategoryFlagCollection, fraudCategoryFlag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fraudCategoryFlag);
      });

      it('should add only unique FraudCategoryFlag to an array', () => {
        const fraudCategoryFlagArray: IFraudCategoryFlag[] = [{ id: 123 }, { id: 456 }, { id: 25906 }];
        const fraudCategoryFlagCollection: IFraudCategoryFlag[] = [{ id: 123 }];
        expectedResult = service.addFraudCategoryFlagToCollectionIfMissing(fraudCategoryFlagCollection, ...fraudCategoryFlagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fraudCategoryFlag: IFraudCategoryFlag = { id: 123 };
        const fraudCategoryFlag2: IFraudCategoryFlag = { id: 456 };
        expectedResult = service.addFraudCategoryFlagToCollectionIfMissing([], fraudCategoryFlag, fraudCategoryFlag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fraudCategoryFlag);
        expect(expectedResult).toContain(fraudCategoryFlag2);
      });

      it('should accept null and undefined values', () => {
        const fraudCategoryFlag: IFraudCategoryFlag = { id: 123 };
        expectedResult = service.addFraudCategoryFlagToCollectionIfMissing([], null, fraudCategoryFlag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fraudCategoryFlag);
      });

      it('should return initial array if no FraudCategoryFlag is added', () => {
        const fraudCategoryFlagCollection: IFraudCategoryFlag[] = [{ id: 123 }];
        expectedResult = service.addFraudCategoryFlagToCollectionIfMissing(fraudCategoryFlagCollection, undefined, null);
        expect(expectedResult).toEqual(fraudCategoryFlagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
