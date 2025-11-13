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
import { WorkInProgressTransferType } from 'app/entities/enumerations/work-in-progress-transfer-type.model';
import { IWorkInProgressTransfer, WorkInProgressTransfer } from '../work-in-progress-transfer.model';

import { WorkInProgressTransferService } from './work-in-progress-transfer.service';

describe('WorkInProgressTransfer Service', () => {
  let service: WorkInProgressTransferService;
  let httpMock: HttpTestingController;
  let elemDefault: IWorkInProgressTransfer;
  let expectedResult: IWorkInProgressTransfer | IWorkInProgressTransfer[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkInProgressTransferService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      targetAssetNumber: 'AAAAAAA',
      transferAmount: 0,
      transferDate: currentDate,
      transferType: WorkInProgressTransferType.DEBIT_TRANSFER,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          transferDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a WorkInProgressTransfer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          transferDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transferDate: currentDate,
        },
        returnedFromService
      );

      service.create(new WorkInProgressTransfer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkInProgressTransfer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          targetAssetNumber: 'BBBBBB',
          transferAmount: 1,
          transferDate: currentDate.format(DATE_FORMAT),
          transferType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transferDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkInProgressTransfer', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          transferType: 'BBBBBB',
        },
        new WorkInProgressTransfer()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          transferDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkInProgressTransfer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          targetAssetNumber: 'BBBBBB',
          transferAmount: 1,
          transferDate: currentDate.format(DATE_FORMAT),
          transferType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          transferDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a WorkInProgressTransfer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWorkInProgressTransferToCollectionIfMissing', () => {
      it('should add a WorkInProgressTransfer to an empty array', () => {
        const workInProgressTransfer: IWorkInProgressTransfer = { id: 123 };
        expectedResult = service.addWorkInProgressTransferToCollectionIfMissing([], workInProgressTransfer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressTransfer);
      });

      it('should not add a WorkInProgressTransfer to an array that contains it', () => {
        const workInProgressTransfer: IWorkInProgressTransfer = { id: 123 };
        const workInProgressTransferCollection: IWorkInProgressTransfer[] = [
          {
            ...workInProgressTransfer,
          },
          { id: 456 },
        ];
        expectedResult = service.addWorkInProgressTransferToCollectionIfMissing(workInProgressTransferCollection, workInProgressTransfer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkInProgressTransfer to an array that doesn't contain it", () => {
        const workInProgressTransfer: IWorkInProgressTransfer = { id: 123 };
        const workInProgressTransferCollection: IWorkInProgressTransfer[] = [{ id: 456 }];
        expectedResult = service.addWorkInProgressTransferToCollectionIfMissing(workInProgressTransferCollection, workInProgressTransfer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressTransfer);
      });

      it('should add only unique WorkInProgressTransfer to an array', () => {
        const workInProgressTransferArray: IWorkInProgressTransfer[] = [{ id: 123 }, { id: 456 }, { id: 89955 }];
        const workInProgressTransferCollection: IWorkInProgressTransfer[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressTransferToCollectionIfMissing(
          workInProgressTransferCollection,
          ...workInProgressTransferArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workInProgressTransfer: IWorkInProgressTransfer = { id: 123 };
        const workInProgressTransfer2: IWorkInProgressTransfer = { id: 456 };
        expectedResult = service.addWorkInProgressTransferToCollectionIfMissing([], workInProgressTransfer, workInProgressTransfer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workInProgressTransfer);
        expect(expectedResult).toContain(workInProgressTransfer2);
      });

      it('should accept null and undefined values', () => {
        const workInProgressTransfer: IWorkInProgressTransfer = { id: 123 };
        expectedResult = service.addWorkInProgressTransferToCollectionIfMissing([], null, workInProgressTransfer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workInProgressTransfer);
      });

      it('should return initial array if no WorkInProgressTransfer is added', () => {
        const workInProgressTransferCollection: IWorkInProgressTransfer[] = [{ id: 123 }];
        expectedResult = service.addWorkInProgressTransferToCollectionIfMissing(workInProgressTransferCollection, undefined, null);
        expect(expectedResult).toEqual(workInProgressTransferCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
