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

import { IDerivativeUnderlyingAsset, DerivativeUnderlyingAsset } from '../derivative-underlying-asset.model';

import { DerivativeUnderlyingAssetService } from './derivative-underlying-asset.service';

describe('DerivativeUnderlyingAsset Service', () => {
  let service: DerivativeUnderlyingAssetService;
  let httpMock: HttpTestingController;
  let elemDefault: IDerivativeUnderlyingAsset;
  let expectedResult: IDerivativeUnderlyingAsset | IDerivativeUnderlyingAsset[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DerivativeUnderlyingAssetService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      derivativeUnderlyingAssetTypeCode: 'AAAAAAA',
      financialDerivativeUnderlyingAssetType: 'AAAAAAA',
      derivativeUnderlyingAssetTypeDetails: 'AAAAAAA',
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

    it('should create a DerivativeUnderlyingAsset', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DerivativeUnderlyingAsset()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DerivativeUnderlyingAsset', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          derivativeUnderlyingAssetTypeCode: 'BBBBBB',
          financialDerivativeUnderlyingAssetType: 'BBBBBB',
          derivativeUnderlyingAssetTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DerivativeUnderlyingAsset', () => {
      const patchObject = Object.assign(
        {
          financialDerivativeUnderlyingAssetType: 'BBBBBB',
          derivativeUnderlyingAssetTypeDetails: 'BBBBBB',
        },
        new DerivativeUnderlyingAsset()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DerivativeUnderlyingAsset', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          derivativeUnderlyingAssetTypeCode: 'BBBBBB',
          financialDerivativeUnderlyingAssetType: 'BBBBBB',
          derivativeUnderlyingAssetTypeDetails: 'BBBBBB',
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

    it('should delete a DerivativeUnderlyingAsset', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDerivativeUnderlyingAssetToCollectionIfMissing', () => {
      it('should add a DerivativeUnderlyingAsset to an empty array', () => {
        const derivativeUnderlyingAsset: IDerivativeUnderlyingAsset = { id: 123 };
        expectedResult = service.addDerivativeUnderlyingAssetToCollectionIfMissing([], derivativeUnderlyingAsset);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(derivativeUnderlyingAsset);
      });

      it('should not add a DerivativeUnderlyingAsset to an array that contains it', () => {
        const derivativeUnderlyingAsset: IDerivativeUnderlyingAsset = { id: 123 };
        const derivativeUnderlyingAssetCollection: IDerivativeUnderlyingAsset[] = [
          {
            ...derivativeUnderlyingAsset,
          },
          { id: 456 },
        ];
        expectedResult = service.addDerivativeUnderlyingAssetToCollectionIfMissing(
          derivativeUnderlyingAssetCollection,
          derivativeUnderlyingAsset
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DerivativeUnderlyingAsset to an array that doesn't contain it", () => {
        const derivativeUnderlyingAsset: IDerivativeUnderlyingAsset = { id: 123 };
        const derivativeUnderlyingAssetCollection: IDerivativeUnderlyingAsset[] = [{ id: 456 }];
        expectedResult = service.addDerivativeUnderlyingAssetToCollectionIfMissing(
          derivativeUnderlyingAssetCollection,
          derivativeUnderlyingAsset
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(derivativeUnderlyingAsset);
      });

      it('should add only unique DerivativeUnderlyingAsset to an array', () => {
        const derivativeUnderlyingAssetArray: IDerivativeUnderlyingAsset[] = [{ id: 123 }, { id: 456 }, { id: 79820 }];
        const derivativeUnderlyingAssetCollection: IDerivativeUnderlyingAsset[] = [{ id: 123 }];
        expectedResult = service.addDerivativeUnderlyingAssetToCollectionIfMissing(
          derivativeUnderlyingAssetCollection,
          ...derivativeUnderlyingAssetArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const derivativeUnderlyingAsset: IDerivativeUnderlyingAsset = { id: 123 };
        const derivativeUnderlyingAsset2: IDerivativeUnderlyingAsset = { id: 456 };
        expectedResult = service.addDerivativeUnderlyingAssetToCollectionIfMissing(
          [],
          derivativeUnderlyingAsset,
          derivativeUnderlyingAsset2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(derivativeUnderlyingAsset);
        expect(expectedResult).toContain(derivativeUnderlyingAsset2);
      });

      it('should accept null and undefined values', () => {
        const derivativeUnderlyingAsset: IDerivativeUnderlyingAsset = { id: 123 };
        expectedResult = service.addDerivativeUnderlyingAssetToCollectionIfMissing([], null, derivativeUnderlyingAsset, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(derivativeUnderlyingAsset);
      });

      it('should return initial array if no DerivativeUnderlyingAsset is added', () => {
        const derivativeUnderlyingAssetCollection: IDerivativeUnderlyingAsset[] = [{ id: 123 }];
        expectedResult = service.addDerivativeUnderlyingAssetToCollectionIfMissing(derivativeUnderlyingAssetCollection, undefined, null);
        expect(expectedResult).toEqual(derivativeUnderlyingAssetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
