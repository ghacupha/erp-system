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

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAssetRevaluation, AssetRevaluation } from '../asset-revaluation.model';

import { AssetRevaluationService } from './asset-revaluation.service';

describe('AssetRevaluation Service', () => {
  let service: AssetRevaluationService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetRevaluation;
  let expectedResult: IAssetRevaluation | IAssetRevaluation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetRevaluationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      devaluationAmount: 0,
      revaluationDate: currentDate,
      revaluationReferenceId: 'AAAAAAA',
      timeOfCreation: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          revaluationDate: currentDate.format(DATE_FORMAT),
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetRevaluation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          revaluationDate: currentDate.format(DATE_FORMAT),
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          revaluationDate: currentDate,
          timeOfCreation: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetRevaluation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetRevaluation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          devaluationAmount: 1,
          revaluationDate: currentDate.format(DATE_FORMAT),
          revaluationReferenceId: 'BBBBBB',
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          revaluationDate: currentDate,
          timeOfCreation: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetRevaluation', () => {
      const patchObject = Object.assign(
        {
          devaluationAmount: 1,
          revaluationReferenceId: 'BBBBBB',
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        new AssetRevaluation()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          revaluationDate: currentDate,
          timeOfCreation: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetRevaluation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          devaluationAmount: 1,
          revaluationDate: currentDate.format(DATE_FORMAT),
          revaluationReferenceId: 'BBBBBB',
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          revaluationDate: currentDate,
          timeOfCreation: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AssetRevaluation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetRevaluationToCollectionIfMissing', () => {
      it('should add a AssetRevaluation to an empty array', () => {
        const assetRevaluation: IAssetRevaluation = { id: 123 };
        expectedResult = service.addAssetRevaluationToCollectionIfMissing([], assetRevaluation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetRevaluation);
      });

      it('should not add a AssetRevaluation to an array that contains it', () => {
        const assetRevaluation: IAssetRevaluation = { id: 123 };
        const assetRevaluationCollection: IAssetRevaluation[] = [
          {
            ...assetRevaluation,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetRevaluationToCollectionIfMissing(assetRevaluationCollection, assetRevaluation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetRevaluation to an array that doesn't contain it", () => {
        const assetRevaluation: IAssetRevaluation = { id: 123 };
        const assetRevaluationCollection: IAssetRevaluation[] = [{ id: 456 }];
        expectedResult = service.addAssetRevaluationToCollectionIfMissing(assetRevaluationCollection, assetRevaluation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetRevaluation);
      });

      it('should add only unique AssetRevaluation to an array', () => {
        const assetRevaluationArray: IAssetRevaluation[] = [{ id: 123 }, { id: 456 }, { id: 52196 }];
        const assetRevaluationCollection: IAssetRevaluation[] = [{ id: 123 }];
        expectedResult = service.addAssetRevaluationToCollectionIfMissing(assetRevaluationCollection, ...assetRevaluationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetRevaluation: IAssetRevaluation = { id: 123 };
        const assetRevaluation2: IAssetRevaluation = { id: 456 };
        expectedResult = service.addAssetRevaluationToCollectionIfMissing([], assetRevaluation, assetRevaluation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetRevaluation);
        expect(expectedResult).toContain(assetRevaluation2);
      });

      it('should accept null and undefined values', () => {
        const assetRevaluation: IAssetRevaluation = { id: 123 };
        expectedResult = service.addAssetRevaluationToCollectionIfMissing([], null, assetRevaluation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetRevaluation);
      });

      it('should return initial array if no AssetRevaluation is added', () => {
        const assetRevaluationCollection: IAssetRevaluation[] = [{ id: 123 }];
        expectedResult = service.addAssetRevaluationToCollectionIfMissing(assetRevaluationCollection, undefined, null);
        expect(expectedResult).toEqual(assetRevaluationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
