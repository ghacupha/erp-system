///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { IFxRateType, FxRateType } from '../fx-rate-type.model';

import { FxRateTypeService } from './fx-rate-type.service';

describe('FxRateType Service', () => {
  let service: FxRateTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFxRateType;
  let expectedResult: IFxRateType | IFxRateType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FxRateTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fxRateCode: 'AAAAAAA',
      fxRateType: 'AAAAAAA',
      fxRateDetails: 'AAAAAAA',
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

    it('should create a FxRateType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FxRateType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FxRateType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxRateCode: 'BBBBBB',
          fxRateType: 'BBBBBB',
          fxRateDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FxRateType', () => {
      const patchObject = Object.assign(
        {
          fxRateType: 'BBBBBB',
        },
        new FxRateType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FxRateType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxRateCode: 'BBBBBB',
          fxRateType: 'BBBBBB',
          fxRateDetails: 'BBBBBB',
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

    it('should delete a FxRateType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFxRateTypeToCollectionIfMissing', () => {
      it('should add a FxRateType to an empty array', () => {
        const fxRateType: IFxRateType = { id: 123 };
        expectedResult = service.addFxRateTypeToCollectionIfMissing([], fxRateType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxRateType);
      });

      it('should not add a FxRateType to an array that contains it', () => {
        const fxRateType: IFxRateType = { id: 123 };
        const fxRateTypeCollection: IFxRateType[] = [
          {
            ...fxRateType,
          },
          { id: 456 },
        ];
        expectedResult = service.addFxRateTypeToCollectionIfMissing(fxRateTypeCollection, fxRateType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FxRateType to an array that doesn't contain it", () => {
        const fxRateType: IFxRateType = { id: 123 };
        const fxRateTypeCollection: IFxRateType[] = [{ id: 456 }];
        expectedResult = service.addFxRateTypeToCollectionIfMissing(fxRateTypeCollection, fxRateType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxRateType);
      });

      it('should add only unique FxRateType to an array', () => {
        const fxRateTypeArray: IFxRateType[] = [{ id: 123 }, { id: 456 }, { id: 82947 }];
        const fxRateTypeCollection: IFxRateType[] = [{ id: 123 }];
        expectedResult = service.addFxRateTypeToCollectionIfMissing(fxRateTypeCollection, ...fxRateTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fxRateType: IFxRateType = { id: 123 };
        const fxRateType2: IFxRateType = { id: 456 };
        expectedResult = service.addFxRateTypeToCollectionIfMissing([], fxRateType, fxRateType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxRateType);
        expect(expectedResult).toContain(fxRateType2);
      });

      it('should accept null and undefined values', () => {
        const fxRateType: IFxRateType = { id: 123 };
        expectedResult = service.addFxRateTypeToCollectionIfMissing([], null, fxRateType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxRateType);
      });

      it('should return initial array if no FxRateType is added', () => {
        const fxRateTypeCollection: IFxRateType[] = [{ id: 123 }];
        expectedResult = service.addFxRateTypeToCollectionIfMissing(fxRateTypeCollection, undefined, null);
        expect(expectedResult).toEqual(fxRateTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
