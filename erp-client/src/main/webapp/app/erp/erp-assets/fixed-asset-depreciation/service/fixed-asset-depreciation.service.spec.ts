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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFixedAssetDepreciation, FixedAssetDepreciation } from '../fixed-asset-depreciation.model';

import { FixedAssetDepreciationService } from './fixed-asset-depreciation.service';
import { DepreciationRegime } from '../../../erp-common/enumerations/depreciation-regime.model';

describe('FixedAssetDepreciation Service', () => {
  let service: FixedAssetDepreciationService;
  let httpMock: HttpTestingController;
  let elemDefault: IFixedAssetDepreciation;
  let expectedResult: IFixedAssetDepreciation | IFixedAssetDepreciation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FixedAssetDepreciationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      assetNumber: 0,
      serviceOutletCode: 'AAAAAAA',
      assetTag: 'AAAAAAA',
      assetDescription: 'AAAAAAA',
      depreciationDate: currentDate,
      assetCategory: 'AAAAAAA',
      depreciationAmount: 0,
      depreciationRegime: DepreciationRegime.STRAIGHT_LINE_BASIS,
      fileUploadToken: 'AAAAAAA',
      compilationToken: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          depreciationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FixedAssetDepreciation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          depreciationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          depreciationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new FixedAssetDepreciation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FixedAssetDepreciation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetNumber: 1,
          serviceOutletCode: 'BBBBBB',
          assetTag: 'BBBBBB',
          assetDescription: 'BBBBBB',
          depreciationDate: currentDate.format(DATE_FORMAT),
          assetCategory: 'BBBBBB',
          depreciationAmount: 1,
          depreciationRegime: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          depreciationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FixedAssetDepreciation', () => {
      const patchObject = Object.assign(
        {
          assetDescription: 'BBBBBB',
          assetCategory: 'BBBBBB',
          depreciationAmount: 1,
          depreciationRegime: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        new FixedAssetDepreciation()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          depreciationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FixedAssetDepreciation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetNumber: 1,
          serviceOutletCode: 'BBBBBB',
          assetTag: 'BBBBBB',
          assetDescription: 'BBBBBB',
          depreciationDate: currentDate.format(DATE_FORMAT),
          assetCategory: 'BBBBBB',
          depreciationAmount: 1,
          depreciationRegime: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          depreciationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FixedAssetDepreciation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFixedAssetDepreciationToCollectionIfMissing', () => {
      it('should add a FixedAssetDepreciation to an empty array', () => {
        const fixedAssetDepreciation: IFixedAssetDepreciation = { id: 123 };
        expectedResult = service.addFixedAssetDepreciationToCollectionIfMissing([], fixedAssetDepreciation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fixedAssetDepreciation);
      });

      it('should not add a FixedAssetDepreciation to an array that contains it', () => {
        const fixedAssetDepreciation: IFixedAssetDepreciation = { id: 123 };
        const fixedAssetDepreciationCollection: IFixedAssetDepreciation[] = [
          {
            ...fixedAssetDepreciation,
          },
          { id: 456 },
        ];
        expectedResult = service.addFixedAssetDepreciationToCollectionIfMissing(fixedAssetDepreciationCollection, fixedAssetDepreciation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FixedAssetDepreciation to an array that doesn't contain it", () => {
        const fixedAssetDepreciation: IFixedAssetDepreciation = { id: 123 };
        const fixedAssetDepreciationCollection: IFixedAssetDepreciation[] = [{ id: 456 }];
        expectedResult = service.addFixedAssetDepreciationToCollectionIfMissing(fixedAssetDepreciationCollection, fixedAssetDepreciation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fixedAssetDepreciation);
      });

      it('should add only unique FixedAssetDepreciation to an array', () => {
        const fixedAssetDepreciationArray: IFixedAssetDepreciation[] = [{ id: 123 }, { id: 456 }, { id: 21457 }];
        const fixedAssetDepreciationCollection: IFixedAssetDepreciation[] = [{ id: 123 }];
        expectedResult = service.addFixedAssetDepreciationToCollectionIfMissing(
          fixedAssetDepreciationCollection,
          ...fixedAssetDepreciationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fixedAssetDepreciation: IFixedAssetDepreciation = { id: 123 };
        const fixedAssetDepreciation2: IFixedAssetDepreciation = { id: 456 };
        expectedResult = service.addFixedAssetDepreciationToCollectionIfMissing([], fixedAssetDepreciation, fixedAssetDepreciation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fixedAssetDepreciation);
        expect(expectedResult).toContain(fixedAssetDepreciation2);
      });

      it('should accept null and undefined values', () => {
        const fixedAssetDepreciation: IFixedAssetDepreciation = { id: 123 };
        expectedResult = service.addFixedAssetDepreciationToCollectionIfMissing([], null, fixedAssetDepreciation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fixedAssetDepreciation);
      });

      it('should return initial array if no FixedAssetDepreciation is added', () => {
        const fixedAssetDepreciationCollection: IFixedAssetDepreciation[] = [{ id: 123 }];
        expectedResult = service.addFixedAssetDepreciationToCollectionIfMissing(fixedAssetDepreciationCollection, undefined, null);
        expect(expectedResult).toEqual(fixedAssetDepreciationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
