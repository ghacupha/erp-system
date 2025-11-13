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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAssetWriteOff, AssetWriteOff } from '../asset-write-off.model';

import { AssetWriteOffService } from './asset-write-off.service';

describe('AssetWriteOff Service', () => {
  let service: AssetWriteOffService;
  let httpMock: HttpTestingController;
  let elemDefault: IAssetWriteOff;
  let expectedResult: IAssetWriteOff | IAssetWriteOff[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AssetWriteOffService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      writeOffAmount: 0,
      writeOffDate: currentDate,
      writeOffReferenceId: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          writeOffDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AssetWriteOff', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          writeOffDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          writeOffDate: currentDate,
        },
        returnedFromService
      );

      service.create(new AssetWriteOff()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AssetWriteOff', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          writeOffAmount: 1,
          writeOffDate: currentDate.format(DATE_FORMAT),
          writeOffReferenceId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          writeOffDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AssetWriteOff', () => {
      const patchObject = Object.assign(
        {
          writeOffReferenceId: 'BBBBBB',
        },
        new AssetWriteOff()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          writeOffDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AssetWriteOff', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          writeOffAmount: 1,
          writeOffDate: currentDate.format(DATE_FORMAT),
          writeOffReferenceId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          writeOffDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AssetWriteOff', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAssetWriteOffToCollectionIfMissing', () => {
      it('should add a AssetWriteOff to an empty array', () => {
        const assetWriteOff: IAssetWriteOff = { id: 123 };
        expectedResult = service.addAssetWriteOffToCollectionIfMissing([], assetWriteOff);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetWriteOff);
      });

      it('should not add a AssetWriteOff to an array that contains it', () => {
        const assetWriteOff: IAssetWriteOff = { id: 123 };
        const assetWriteOffCollection: IAssetWriteOff[] = [
          {
            ...assetWriteOff,
          },
          { id: 456 },
        ];
        expectedResult = service.addAssetWriteOffToCollectionIfMissing(assetWriteOffCollection, assetWriteOff);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AssetWriteOff to an array that doesn't contain it", () => {
        const assetWriteOff: IAssetWriteOff = { id: 123 };
        const assetWriteOffCollection: IAssetWriteOff[] = [{ id: 456 }];
        expectedResult = service.addAssetWriteOffToCollectionIfMissing(assetWriteOffCollection, assetWriteOff);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetWriteOff);
      });

      it('should add only unique AssetWriteOff to an array', () => {
        const assetWriteOffArray: IAssetWriteOff[] = [{ id: 123 }, { id: 456 }, { id: 85312 }];
        const assetWriteOffCollection: IAssetWriteOff[] = [{ id: 123 }];
        expectedResult = service.addAssetWriteOffToCollectionIfMissing(assetWriteOffCollection, ...assetWriteOffArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const assetWriteOff: IAssetWriteOff = { id: 123 };
        const assetWriteOff2: IAssetWriteOff = { id: 456 };
        expectedResult = service.addAssetWriteOffToCollectionIfMissing([], assetWriteOff, assetWriteOff2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(assetWriteOff);
        expect(expectedResult).toContain(assetWriteOff2);
      });

      it('should accept null and undefined values', () => {
        const assetWriteOff: IAssetWriteOff = { id: 123 };
        expectedResult = service.addAssetWriteOffToCollectionIfMissing([], null, assetWriteOff, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(assetWriteOff);
      });

      it('should return initial array if no AssetWriteOff is added', () => {
        const assetWriteOffCollection: IAssetWriteOff[] = [{ id: 123 }];
        expectedResult = service.addAssetWriteOffToCollectionIfMissing(assetWriteOffCollection, undefined, null);
        expect(expectedResult).toEqual(assetWriteOffCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
