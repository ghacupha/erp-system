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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILeaseModelMetadata, LeaseModelMetadata } from '../lease-model-metadata.model';

import { LeaseModelMetadataService } from './lease-model-metadata.service';

describe('LeaseModelMetadata Service', () => {
  let service: LeaseModelMetadataService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseModelMetadata;
  let expectedResult: ILeaseModelMetadata | ILeaseModelMetadata[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseModelMetadataService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      modelTitle: 'AAAAAAA',
      modelVersion: 0,
      description: 'AAAAAAA',
      modelNotesContentType: 'image/png',
      modelNotes: 'AAAAAAA',
      annualDiscountingRate: 0,
      commencementDate: currentDate,
      terminalDate: currentDate,
      totalReportingPeriods: 0,
      reportingPeriodsPerYear: 0,
      settlementPeriodsPerYear: 0,
      initialLiabilityAmount: 0,
      initialROUAmount: 0,
      totalDepreciationPeriods: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          commencementDate: currentDate.format(DATE_FORMAT),
          terminalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a LeaseModelMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          commencementDate: currentDate.format(DATE_FORMAT),
          terminalDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          terminalDate: currentDate,
        },
        returnedFromService
      );

      service.create(new LeaseModelMetadata()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseModelMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          modelTitle: 'BBBBBB',
          modelVersion: 1,
          description: 'BBBBBB',
          modelNotes: 'BBBBBB',
          annualDiscountingRate: 1,
          commencementDate: currentDate.format(DATE_FORMAT),
          terminalDate: currentDate.format(DATE_FORMAT),
          totalReportingPeriods: 1,
          reportingPeriodsPerYear: 1,
          settlementPeriodsPerYear: 1,
          initialLiabilityAmount: 1,
          initialROUAmount: 1,
          totalDepreciationPeriods: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          terminalDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaseModelMetadata', () => {
      const patchObject = Object.assign(
        {
          modelTitle: 'BBBBBB',
          modelVersion: 1,
          modelNotes: 'BBBBBB',
          initialROUAmount: 1,
        },
        new LeaseModelMetadata()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          terminalDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaseModelMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          modelTitle: 'BBBBBB',
          modelVersion: 1,
          description: 'BBBBBB',
          modelNotes: 'BBBBBB',
          annualDiscountingRate: 1,
          commencementDate: currentDate.format(DATE_FORMAT),
          terminalDate: currentDate.format(DATE_FORMAT),
          totalReportingPeriods: 1,
          reportingPeriodsPerYear: 1,
          settlementPeriodsPerYear: 1,
          initialLiabilityAmount: 1,
          initialROUAmount: 1,
          totalDepreciationPeriods: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commencementDate: currentDate,
          terminalDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LeaseModelMetadata', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseModelMetadataToCollectionIfMissing', () => {
      it('should add a LeaseModelMetadata to an empty array', () => {
        const leaseModelMetadata: ILeaseModelMetadata = { id: 123 };
        expectedResult = service.addLeaseModelMetadataToCollectionIfMissing([], leaseModelMetadata);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseModelMetadata);
      });

      it('should not add a LeaseModelMetadata to an array that contains it', () => {
        const leaseModelMetadata: ILeaseModelMetadata = { id: 123 };
        const leaseModelMetadataCollection: ILeaseModelMetadata[] = [
          {
            ...leaseModelMetadata,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseModelMetadataToCollectionIfMissing(leaseModelMetadataCollection, leaseModelMetadata);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseModelMetadata to an array that doesn't contain it", () => {
        const leaseModelMetadata: ILeaseModelMetadata = { id: 123 };
        const leaseModelMetadataCollection: ILeaseModelMetadata[] = [{ id: 456 }];
        expectedResult = service.addLeaseModelMetadataToCollectionIfMissing(leaseModelMetadataCollection, leaseModelMetadata);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseModelMetadata);
      });

      it('should add only unique LeaseModelMetadata to an array', () => {
        const leaseModelMetadataArray: ILeaseModelMetadata[] = [{ id: 123 }, { id: 456 }, { id: 93121 }];
        const leaseModelMetadataCollection: ILeaseModelMetadata[] = [{ id: 123 }];
        expectedResult = service.addLeaseModelMetadataToCollectionIfMissing(leaseModelMetadataCollection, ...leaseModelMetadataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseModelMetadata: ILeaseModelMetadata = { id: 123 };
        const leaseModelMetadata2: ILeaseModelMetadata = { id: 456 };
        expectedResult = service.addLeaseModelMetadataToCollectionIfMissing([], leaseModelMetadata, leaseModelMetadata2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseModelMetadata);
        expect(expectedResult).toContain(leaseModelMetadata2);
      });

      it('should accept null and undefined values', () => {
        const leaseModelMetadata: ILeaseModelMetadata = { id: 123 };
        expectedResult = service.addLeaseModelMetadataToCollectionIfMissing([], null, leaseModelMetadata, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseModelMetadata);
      });

      it('should return initial array if no LeaseModelMetadata is added', () => {
        const leaseModelMetadataCollection: ILeaseModelMetadata[] = [{ id: 123 }];
        expectedResult = service.addLeaseModelMetadataToCollectionIfMissing(leaseModelMetadataCollection, undefined, null);
        expect(expectedResult).toEqual(leaseModelMetadataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
