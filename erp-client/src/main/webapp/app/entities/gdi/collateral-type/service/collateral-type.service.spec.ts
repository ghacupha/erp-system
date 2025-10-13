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

import { ICollateralType, CollateralType } from '../collateral-type.model';

import { CollateralTypeService } from './collateral-type.service';

describe('CollateralType Service', () => {
  let service: CollateralTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICollateralType;
  let expectedResult: ICollateralType | ICollateralType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CollateralTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      collateralTypeCode: 'AAAAAAA',
      collateralType: 'AAAAAAA',
      collateralTypeDescription: 'AAAAAAA',
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

    it('should create a CollateralType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CollateralType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CollateralType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          collateralTypeCode: 'BBBBBB',
          collateralType: 'BBBBBB',
          collateralTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CollateralType', () => {
      const patchObject = Object.assign(
        {
          collateralTypeCode: 'BBBBBB',
          collateralType: 'BBBBBB',
        },
        new CollateralType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CollateralType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          collateralTypeCode: 'BBBBBB',
          collateralType: 'BBBBBB',
          collateralTypeDescription: 'BBBBBB',
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

    it('should delete a CollateralType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCollateralTypeToCollectionIfMissing', () => {
      it('should add a CollateralType to an empty array', () => {
        const collateralType: ICollateralType = { id: 123 };
        expectedResult = service.addCollateralTypeToCollectionIfMissing([], collateralType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collateralType);
      });

      it('should not add a CollateralType to an array that contains it', () => {
        const collateralType: ICollateralType = { id: 123 };
        const collateralTypeCollection: ICollateralType[] = [
          {
            ...collateralType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCollateralTypeToCollectionIfMissing(collateralTypeCollection, collateralType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CollateralType to an array that doesn't contain it", () => {
        const collateralType: ICollateralType = { id: 123 };
        const collateralTypeCollection: ICollateralType[] = [{ id: 456 }];
        expectedResult = service.addCollateralTypeToCollectionIfMissing(collateralTypeCollection, collateralType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collateralType);
      });

      it('should add only unique CollateralType to an array', () => {
        const collateralTypeArray: ICollateralType[] = [{ id: 123 }, { id: 456 }, { id: 87044 }];
        const collateralTypeCollection: ICollateralType[] = [{ id: 123 }];
        expectedResult = service.addCollateralTypeToCollectionIfMissing(collateralTypeCollection, ...collateralTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const collateralType: ICollateralType = { id: 123 };
        const collateralType2: ICollateralType = { id: 456 };
        expectedResult = service.addCollateralTypeToCollectionIfMissing([], collateralType, collateralType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collateralType);
        expect(expectedResult).toContain(collateralType2);
      });

      it('should accept null and undefined values', () => {
        const collateralType: ICollateralType = { id: 123 };
        expectedResult = service.addCollateralTypeToCollectionIfMissing([], null, collateralType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collateralType);
      });

      it('should return initial array if no CollateralType is added', () => {
        const collateralTypeCollection: ICollateralType[] = [{ id: 123 }];
        expectedResult = service.addCollateralTypeToCollectionIfMissing(collateralTypeCollection, undefined, null);
        expect(expectedResult).toEqual(collateralTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
