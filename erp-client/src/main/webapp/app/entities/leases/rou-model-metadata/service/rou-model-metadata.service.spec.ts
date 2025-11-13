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
import { IRouModelMetadata, RouModelMetadata } from '../rou-model-metadata.model';

import { RouModelMetadataService } from './rou-model-metadata.service';

describe('RouModelMetadata Service', () => {
  let service: RouModelMetadataService;
  let httpMock: HttpTestingController;
  let elemDefault: IRouModelMetadata;
  let expectedResult: IRouModelMetadata | IRouModelMetadata[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RouModelMetadataService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      modelTitle: 'AAAAAAA',
      modelVersion: 0,
      description: 'AAAAAAA',
      leaseTermPeriods: 0,
      leaseAmount: 0,
      rouModelReference: 'AAAAAAA',
      commencementDate: currentDate,
      expirationDate: currentDate,
      hasBeenFullyAmortised: false,
      hasBeenDecommissioned: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RouModelMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new RouModelMetadata()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RouModelMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          modelTitle: 'BBBBBB',
          modelVersion: 1,
          description: 'BBBBBB',
          leaseTermPeriods: 1,
          leaseAmount: 1,
          rouModelReference: 'BBBBBB',
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
          hasBeenFullyAmortised: true,
          hasBeenDecommissioned: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RouModelMetadata', () => {
      const patchObject = Object.assign(
        {
          modelVersion: 1,
          leaseTermPeriods: 1,
          rouModelReference: 'BBBBBB',
          hasBeenFullyAmortised: true,
        },
        new RouModelMetadata()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RouModelMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          modelTitle: 'BBBBBB',
          modelVersion: 1,
          description: 'BBBBBB',
          leaseTermPeriods: 1,
          leaseAmount: 1,
          rouModelReference: 'BBBBBB',
          commencementDate: currentDate.format(DATE_FORMAT),
          expirationDate: currentDate.format(DATE_FORMAT),
          hasBeenFullyAmortised: true,
          hasBeenDecommissioned: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RouModelMetadata', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRouModelMetadataToCollectionIfMissing', () => {
      it('should add a RouModelMetadata to an empty array', () => {
        const rouModelMetadata: IRouModelMetadata = { id: 123 };
        expectedResult = service.addRouModelMetadataToCollectionIfMissing([], rouModelMetadata);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouModelMetadata);
      });

      it('should not add a RouModelMetadata to an array that contains it', () => {
        const rouModelMetadata: IRouModelMetadata = { id: 123 };
        const rouModelMetadataCollection: IRouModelMetadata[] = [
          {
            ...rouModelMetadata,
          },
          { id: 456 },
        ];
        expectedResult = service.addRouModelMetadataToCollectionIfMissing(rouModelMetadataCollection, rouModelMetadata);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RouModelMetadata to an array that doesn't contain it", () => {
        const rouModelMetadata: IRouModelMetadata = { id: 123 };
        const rouModelMetadataCollection: IRouModelMetadata[] = [{ id: 456 }];
        expectedResult = service.addRouModelMetadataToCollectionIfMissing(rouModelMetadataCollection, rouModelMetadata);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouModelMetadata);
      });

      it('should add only unique RouModelMetadata to an array', () => {
        const rouModelMetadataArray: IRouModelMetadata[] = [{ id: 123 }, { id: 456 }, { id: 84260 }];
        const rouModelMetadataCollection: IRouModelMetadata[] = [{ id: 123 }];
        expectedResult = service.addRouModelMetadataToCollectionIfMissing(rouModelMetadataCollection, ...rouModelMetadataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rouModelMetadata: IRouModelMetadata = { id: 123 };
        const rouModelMetadata2: IRouModelMetadata = { id: 456 };
        expectedResult = service.addRouModelMetadataToCollectionIfMissing([], rouModelMetadata, rouModelMetadata2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rouModelMetadata);
        expect(expectedResult).toContain(rouModelMetadata2);
      });

      it('should accept null and undefined values', () => {
        const rouModelMetadata: IRouModelMetadata = { id: 123 };
        expectedResult = service.addRouModelMetadataToCollectionIfMissing([], null, rouModelMetadata, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rouModelMetadata);
      });

      it('should return initial array if no RouModelMetadata is added', () => {
        const rouModelMetadataCollection: IRouModelMetadata[] = [{ id: 123 }];
        expectedResult = service.addRouModelMetadataToCollectionIfMissing(rouModelMetadataCollection, undefined, null);
        expect(expectedResult).toEqual(rouModelMetadataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
