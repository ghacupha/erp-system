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
import { IIFRS16LeaseContract, IFRS16LeaseContract } from '../ifrs-16-lease-contract.model';

import { IFRS16LeaseContractService } from './ifrs-16-lease-contract.service';

describe('IFRS16LeaseContract Service', () => {
  let service: IFRS16LeaseContractService;
  let httpMock: HttpTestingController;
  let elemDefault: IIFRS16LeaseContract;
  let expectedResult: IIFRS16LeaseContract | IIFRS16LeaseContract[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IFRS16LeaseContractService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      bookingId: 'AAAAAAA',
      leaseTitle: 'AAAAAAA',
      shortTitle: 'AAAAAAA',
      description: 'AAAAAAA',
      inceptionDate: currentDate,
      commencementDate: currentDate,
      serialNumber: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          inceptionDate: currentDate.format(DATE_FORMAT),
          commencementDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a IFRS16LeaseContract', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          inceptionDate: currentDate.format(DATE_FORMAT),
          commencementDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inceptionDate: currentDate,
          commencementDate: currentDate,
        },
        returnedFromService
      );

      service.create(new IFRS16LeaseContract()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IFRS16LeaseContract', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bookingId: 'BBBBBB',
          leaseTitle: 'BBBBBB',
          shortTitle: 'BBBBBB',
          description: 'BBBBBB',
          inceptionDate: currentDate.format(DATE_FORMAT),
          commencementDate: currentDate.format(DATE_FORMAT),
          serialNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inceptionDate: currentDate,
          commencementDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IFRS16LeaseContract', () => {
      const patchObject = Object.assign(
        {
          bookingId: 'BBBBBB',
          leaseTitle: 'BBBBBB',
        },
        new IFRS16LeaseContract()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          inceptionDate: currentDate,
          commencementDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IFRS16LeaseContract', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bookingId: 'BBBBBB',
          leaseTitle: 'BBBBBB',
          shortTitle: 'BBBBBB',
          description: 'BBBBBB',
          inceptionDate: currentDate.format(DATE_FORMAT),
          commencementDate: currentDate.format(DATE_FORMAT),
          serialNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inceptionDate: currentDate,
          commencementDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a IFRS16LeaseContract', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIFRS16LeaseContractToCollectionIfMissing', () => {
      it('should add a IFRS16LeaseContract to an empty array', () => {
        const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 123 };
        expectedResult = service.addIFRS16LeaseContractToCollectionIfMissing([], iFRS16LeaseContract);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(iFRS16LeaseContract);
      });

      it('should not add a IFRS16LeaseContract to an array that contains it', () => {
        const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 123 };
        const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [
          {
            ...iFRS16LeaseContract,
          },
          { id: 456 },
        ];
        expectedResult = service.addIFRS16LeaseContractToCollectionIfMissing(iFRS16LeaseContractCollection, iFRS16LeaseContract);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IFRS16LeaseContract to an array that doesn't contain it", () => {
        const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 123 };
        const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 456 }];
        expectedResult = service.addIFRS16LeaseContractToCollectionIfMissing(iFRS16LeaseContractCollection, iFRS16LeaseContract);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(iFRS16LeaseContract);
      });

      it('should add only unique IFRS16LeaseContract to an array', () => {
        const iFRS16LeaseContractArray: IIFRS16LeaseContract[] = [{ id: 123 }, { id: 456 }, { id: 91733 }];
        const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 123 }];
        expectedResult = service.addIFRS16LeaseContractToCollectionIfMissing(iFRS16LeaseContractCollection, ...iFRS16LeaseContractArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 123 };
        const iFRS16LeaseContract2: IIFRS16LeaseContract = { id: 456 };
        expectedResult = service.addIFRS16LeaseContractToCollectionIfMissing([], iFRS16LeaseContract, iFRS16LeaseContract2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(iFRS16LeaseContract);
        expect(expectedResult).toContain(iFRS16LeaseContract2);
      });

      it('should accept null and undefined values', () => {
        const iFRS16LeaseContract: IIFRS16LeaseContract = { id: 123 };
        expectedResult = service.addIFRS16LeaseContractToCollectionIfMissing([], null, iFRS16LeaseContract, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(iFRS16LeaseContract);
      });

      it('should return initial array if no IFRS16LeaseContract is added', () => {
        const iFRS16LeaseContractCollection: IIFRS16LeaseContract[] = [{ id: 123 }];
        expectedResult = service.addIFRS16LeaseContractToCollectionIfMissing(iFRS16LeaseContractCollection, undefined, null);
        expect(expectedResult).toEqual(iFRS16LeaseContractCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
