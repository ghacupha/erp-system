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
import { IAssetGeneralAdjustment, AssetGeneralAdjustment } from '../asset-general-adjustment.model';

import { AssetGeneralAdjustmentService } from './asset-general-adjustment.service';

describe('AssetGeneralAdjustment Service', () => {
  let service: AssetGeneralAdjustmentService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetGeneralAdjustment;
  let expectedResult: IAssetGeneralAdjustment | IAssetGeneralAdjustment[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetGeneralAdjustmentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      devaluationAmount: 0,
      adjustmentDate: currentDate,
      timeOfCreation: currentDate,
      adjustmentReferenceId: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          adjustmentDate: currentDate.format(DATE_FORMAT),
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetGeneralAdjustment', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          adjustmentDate: currentDate.format(DATE_FORMAT),
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          adjustmentDate: currentDate,
          timeOfCreation: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetGeneralAdjustment()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetGeneralAdjustment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          devaluationAmount: 1,
          adjustmentDate: currentDate.format(DATE_FORMAT),
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
          adjustmentReferenceId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          adjustmentDate: currentDate,
          timeOfCreation: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetGeneralAdjustment', () => {
      const patchObject = Object.assign(
        {
          devaluationAmount: 1,
          adjustmentDate: currentDate.format(DATE_FORMAT),
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
        },
        new AssetGeneralAdjustment()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          adjustmentDate: currentDate,
          timeOfCreation: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetGeneralAdjustment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          devaluationAmount: 1,
          adjustmentDate: currentDate.format(DATE_FORMAT),
          timeOfCreation: currentDate.format(DATE_TIME_FORMAT),
          adjustmentReferenceId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          adjustmentDate: currentDate,
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

    it('should delete a AssetGeneralAdjustment', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetGeneralAdjustmentToCollectionIfMissing', () => {
      it('should add a AssetGeneralAdjustment to an empty array', () => {
        const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 123 };
        expectedResult = service.addAssetGeneralAdjustmentToCollectionIfMissing([], assetGeneralAdjustment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetGeneralAdjustment);
      });

      it('should not add a AssetGeneralAdjustment to an array that contains it', () => {
        const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 123 };
        const assetGeneralAdjustmentCollection: IAssetGeneralAdjustment[] = [
          {
            ...assetGeneralAdjustment,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetGeneralAdjustmentToCollectionIfMissing(assetGeneralAdjustmentCollection, assetGeneralAdjustment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetGeneralAdjustment to an array that doesn't contain it", () => {
        const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 123 };
        const assetGeneralAdjustmentCollection: IAssetGeneralAdjustment[] = [{ id: 456 }];
        expectedResult = service.addAssetGeneralAdjustmentToCollectionIfMissing(assetGeneralAdjustmentCollection, assetGeneralAdjustment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetGeneralAdjustment);
      });

      it('should add only unique AssetGeneralAdjustment to an array', () => {
        const assetGeneralAdjustmentArray: IAssetGeneralAdjustment[] = [{ id: 123 }, { id: 456 }, { id: 43109 }];
        const assetGeneralAdjustmentCollection: IAssetGeneralAdjustment[] = [{ id: 123 }];
        expectedResult = service.addAssetGeneralAdjustmentToCollectionIfMissing(
          assetGeneralAdjustmentCollection,
          ...assetGeneralAdjustmentArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 123 };
        const assetGeneralAdjustment2: IAssetGeneralAdjustment = { id: 456 };
        expectedResult = service.addAssetGeneralAdjustmentToCollectionIfMissing([], assetGeneralAdjustment, assetGeneralAdjustment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetGeneralAdjustment);
        expect(expectedResult).toContain(assetGeneralAdjustment2);
      });

      it('should accept null and undefined values', () => {
        const assetGeneralAdjustment: IAssetGeneralAdjustment = { id: 123 };
        expectedResult = service.addAssetGeneralAdjustmentToCollectionIfMissing([], null, assetGeneralAdjustment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetGeneralAdjustment);
      });

      it('should return initial array if no AssetGeneralAdjustment is added', () => {
        const assetGeneralAdjustmentCollection: IAssetGeneralAdjustment[] = [{ id: 123 }];
        expectedResult = service.addAssetGeneralAdjustmentToCollectionIfMissing(assetGeneralAdjustmentCollection, undefined, null);
        expect(expectedResult).toEqual(assetGeneralAdjustmentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
