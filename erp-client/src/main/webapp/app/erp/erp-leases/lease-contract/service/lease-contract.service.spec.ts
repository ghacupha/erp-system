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
import { ILeaseContract, LeaseContract } from '../lease-contract.model';

import { LeaseContractService } from './lease-contract.service';

describe('LeaseContract Service', () => {
  let service: LeaseContractService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseContract;
  let expectedResult: ILeaseContract | ILeaseContract[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseContractService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      bookingId: 'AAAAAAA',
      leaseTitle: 'AAAAAAA',
      identifier: 'AAAAAAA',
      description: 'AAAAAAA',
      commencementDate: currentDate,
      terminalDate: currentDate,
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

    it('should create a LeaseContract', () => {
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

      service.create(new LeaseContract()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseContract', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bookingId: 'BBBBBB',
          leaseTitle: 'BBBBBB',
          identifier: 'BBBBBB',
          description: 'BBBBBB',
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

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaseContract', () => {
      const patchObject = Object.assign(
        {
          bookingId: 'BBBBBB',
          identifier: 'BBBBBB',
        },
        new LeaseContract()
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

    it('should return a list of LeaseContract', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bookingId: 'BBBBBB',
          leaseTitle: 'BBBBBB',
          identifier: 'BBBBBB',
          description: 'BBBBBB',
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

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a LeaseContract', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseContractToCollectionIfMissing', () => {
      it('should add a LeaseContract to an empty array', () => {
        const leaseContract: ILeaseContract = { id: 123 };
        expectedResult = service.addLeaseContractToCollectionIfMissing([], leaseContract);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseContract);
      });

      it('should not add a LeaseContract to an array that contains it', () => {
        const leaseContract: ILeaseContract = { id: 123 };
        const leaseContractCollection: ILeaseContract[] = [
          {
            ...leaseContract,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseContractToCollectionIfMissing(leaseContractCollection, leaseContract);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseContract to an array that doesn't contain it", () => {
        const leaseContract: ILeaseContract = { id: 123 };
        const leaseContractCollection: ILeaseContract[] = [{ id: 456 }];
        expectedResult = service.addLeaseContractToCollectionIfMissing(leaseContractCollection, leaseContract);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseContract);
      });

      it('should add only unique LeaseContract to an array', () => {
        const leaseContractArray: ILeaseContract[] = [{ id: 123 }, { id: 456 }, { id: 69189 }];
        const leaseContractCollection: ILeaseContract[] = [{ id: 123 }];
        expectedResult = service.addLeaseContractToCollectionIfMissing(leaseContractCollection, ...leaseContractArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseContract: ILeaseContract = { id: 123 };
        const leaseContract2: ILeaseContract = { id: 456 };
        expectedResult = service.addLeaseContractToCollectionIfMissing([], leaseContract, leaseContract2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseContract);
        expect(expectedResult).toContain(leaseContract2);
      });

      it('should accept null and undefined values', () => {
        const leaseContract: ILeaseContract = { id: 123 };
        expectedResult = service.addLeaseContractToCollectionIfMissing([], null, leaseContract, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseContract);
      });

      it('should return initial array if no LeaseContract is added', () => {
        const leaseContractCollection: ILeaseContract[] = [{ id: 123 }];
        expectedResult = service.addLeaseContractToCollectionIfMissing(leaseContractCollection, undefined, null);
        expect(expectedResult).toEqual(leaseContractCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
