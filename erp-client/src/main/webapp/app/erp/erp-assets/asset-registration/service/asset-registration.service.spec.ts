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

import { IAssetRegistration, AssetRegistration } from '../asset-registration.model';

import { AssetRegistrationService } from './asset-registration.service';
import * as dayjs from 'dayjs';
import { DATE_FORMAT } from '../../../../config/input.constants';

describe('AssetRegistration Service', () => {
  let service: AssetRegistrationService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetRegistration;
  let expectedResult: IAssetRegistration | IAssetRegistration[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetRegistrationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      assetNumber: 'AAAAAAA',
      assetTag: 'AAAAAAA',
      assetDetails: 'AAAAAAA',
      assetCost: 0,
      commentsContentType: 'image/png',
      comments: 'AAAAAAA',
      modelNumber: 'AAAAAAA',
      serialNumber: 'AAAAAAA',
      remarks: 'AAAAAAA',
      capitalizationDate: currentDate,
      historicalCost: 0,
      registrationDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          capitalizationDate: currentDate.format(DATE_FORMAT),
          registrationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetRegistration', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          capitalizationDate: currentDate.format(DATE_FORMAT),
          registrationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          capitalizationDate: currentDate,
          registrationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetRegistration()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetRegistration', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetNumber: 'BBBBBB',
          assetTag: 'BBBBBB',
          assetDetails: 'BBBBBB',
          assetCost: 1,
          comments: 'BBBBBB',
          modelNumber: 'BBBBBB',
          serialNumber: 'BBBBBB',
          remarks: 'BBBBBB',
          capitalizationDate: currentDate.format(DATE_FORMAT),
          historicalCost: 1,
          registrationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          capitalizationDate: currentDate,
          registrationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetRegistration', () => {
      const patchObject = Object.assign(
        {
          assetNumber: 'BBBBBB',
          assetCost: 1,
          serialNumber: 'BBBBBB',
          historicalCost: 1,
          registrationDate: currentDate.format(DATE_FORMAT),
        },
        new AssetRegistration()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          capitalizationDate: currentDate,
          registrationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetRegistration', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          assetNumber: 'BBBBBB',
          assetTag: 'BBBBBB',
          assetDetails: 'BBBBBB',
          assetCost: 1,
          comments: 'BBBBBB',
          modelNumber: 'BBBBBB',
          serialNumber: 'BBBBBB',
          remarks: 'BBBBBB',
          capitalizationDate: currentDate.format(DATE_FORMAT),
          historicalCost: 1,
          registrationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          capitalizationDate: currentDate,
          registrationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AssetRegistration', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetRegistrationToCollectionIfMissing', () => {
      it('should add a AssetRegistration to an empty array', () => {
        const assetRegistration: IAssetRegistration = { id: 123 };
        expectedResult = service.addAssetRegistrationToCollectionIfMissing([], assetRegistration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetRegistration);
      });

      it('should not add a AssetRegistration to an array that contains it', () => {
        const assetRegistration: IAssetRegistration = { id: 123 };
        const assetRegistrationCollection: IAssetRegistration[] = [
          {
            ...assetRegistration,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetRegistrationToCollectionIfMissing(assetRegistrationCollection, assetRegistration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetRegistration to an array that doesn't contain it", () => {
        const assetRegistration: IAssetRegistration = { id: 123 };
        const assetRegistrationCollection: IAssetRegistration[] = [{ id: 456 }];
        expectedResult = service.addAssetRegistrationToCollectionIfMissing(assetRegistrationCollection, assetRegistration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetRegistration);
      });

      it('should add only unique AssetRegistration to an array', () => {
        const assetRegistrationArray: IAssetRegistration[] = [{ id: 123 }, { id: 456 }, { id: 92082 }];
        const assetRegistrationCollection: IAssetRegistration[] = [{ id: 123 }];
        expectedResult = service.addAssetRegistrationToCollectionIfMissing(assetRegistrationCollection, ...assetRegistrationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetRegistration: IAssetRegistration = { id: 123 };
        const assetRegistration2: IAssetRegistration = { id: 456 };
        expectedResult = service.addAssetRegistrationToCollectionIfMissing([], assetRegistration, assetRegistration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetRegistration);
        expect(expectedResult).toContain(assetRegistration2);
      });

      it('should accept null and undefined values', () => {
        const assetRegistration: IAssetRegistration = { id: 123 };
        expectedResult = service.addAssetRegistrationToCollectionIfMissing([], null, assetRegistration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetRegistration);
      });

      it('should return initial array if no AssetRegistration is added', () => {
        const assetRegistrationCollection: IAssetRegistration[] = [{ id: 123 }];
        expectedResult = service.addAssetRegistrationToCollectionIfMissing(assetRegistrationCollection, undefined, null);
        expect(expectedResult).toEqual(assetRegistrationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
