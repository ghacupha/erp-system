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

import { IContractStatus, ContractStatus } from '../contract-status.model';

import { ContractStatusService } from './contract-status.service';

describe('ContractStatus Service', () => {
  let service: ContractStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IContractStatus;
  let expectedResult: IContractStatus | IContractStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContractStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      contractStatusCode: 'AAAAAAA',
      contractStatusType: 'AAAAAAA',
      contractStatusTypeDescription: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ContractStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ContractStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContractStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          contractStatusCode: 'BBBBBB',
          contractStatusType: 'BBBBBB',
          contractStatusTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContractStatus', () => {
      const patchObject = Object.assign(
        {
          contractStatusCode: 'BBBBBB',
          contractStatusType: 'BBBBBB',
        },
        new ContractStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContractStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          contractStatusCode: 'BBBBBB',
          contractStatusType: 'BBBBBB',
          contractStatusTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ContractStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContractStatusToCollectionIfMissing', () => {
      it('should add a ContractStatus to an empty array', () => {
        const contractStatus: IContractStatus = { id: 123 };
        expectedResult = service.addContractStatusToCollectionIfMissing([], contractStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractStatus);
      });

      it('should not add a ContractStatus to an array that contains it', () => {
        const contractStatus: IContractStatus = { id: 123 };
        const contractStatusCollection: IContractStatus[] = [
          {
            ...contractStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addContractStatusToCollectionIfMissing(contractStatusCollection, contractStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContractStatus to an array that doesn't contain it", () => {
        const contractStatus: IContractStatus = { id: 123 };
        const contractStatusCollection: IContractStatus[] = [{ id: 456 }];
        expectedResult = service.addContractStatusToCollectionIfMissing(contractStatusCollection, contractStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractStatus);
      });

      it('should add only unique ContractStatus to an array', () => {
        const contractStatusArray: IContractStatus[] = [{ id: 123 }, { id: 456 }, { id: 61681 }];
        const contractStatusCollection: IContractStatus[] = [{ id: 123 }];
        expectedResult = service.addContractStatusToCollectionIfMissing(contractStatusCollection, ...contractStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractStatus: IContractStatus = { id: 123 };
        const contractStatus2: IContractStatus = { id: 456 };
        expectedResult = service.addContractStatusToCollectionIfMissing([], contractStatus, contractStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractStatus);
        expect(expectedResult).toContain(contractStatus2);
      });

      it('should accept null and undefined values', () => {
        const contractStatus: IContractStatus = { id: 123 };
        expectedResult = service.addContractStatusToCollectionIfMissing([], null, contractStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractStatus);
      });

      it('should return initial array if no ContractStatus is added', () => {
        const contractStatusCollection: IContractStatus[] = [{ id: 123 }];
        expectedResult = service.addContractStatusToCollectionIfMissing(contractStatusCollection, undefined, null);
        expect(expectedResult).toEqual(contractStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
