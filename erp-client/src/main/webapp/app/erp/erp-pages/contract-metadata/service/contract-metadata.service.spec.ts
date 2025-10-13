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

import { ContractMetadataService } from './contract-metadata.service';
import { IContractMetadata } from '../contract-metadata.model';

describe('ContractMetadata Service', () => {
  let service: ContractMetadataService;
  let httpMock: HttpTestingController;
  let elemDefault: IContractMetadata;
  let expectedResult: IContractMetadata | IContractMetadata[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContractMetadataService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      typeOfContract: ContractType.SUPPLIER,
      contractStatus: ContractStatus.ACTIVE,
      startDate: currentDate,
      terminationDate: currentDate,
      commentsAndAttachment: 'AAAAAAA',
      contractTitle: 'AAAAAAA',
      contractIdentifier: 'AAAAAAA',
      contractIdentifierShort: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_FORMAT),
          terminationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ContractMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startDate: currentDate.format(DATE_FORMAT),
          terminationDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          terminationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new ContractMetadata()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContractMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          typeOfContract: 'BBBBBB',
          contractStatus: 'BBBBBB',
          startDate: currentDate.format(DATE_FORMAT),
          terminationDate: currentDate.format(DATE_FORMAT),
          commentsAndAttachment: 'BBBBBB',
          contractTitle: 'BBBBBB',
          contractIdentifier: 'BBBBBB',
          contractIdentifierShort: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          terminationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContractMetadata', () => {
      const patchObject = Object.assign(
        {
          contractStatus: 'BBBBBB',
          startDate: currentDate.format(DATE_FORMAT),
          commentsAndAttachment: 'BBBBBB',
          contractTitle: 'BBBBBB',
          contractIdentifierShort: 'BBBBBB',
        },
        new ContractMetadata()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
          terminationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContractMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          typeOfContract: 'BBBBBB',
          contractStatus: 'BBBBBB',
          startDate: currentDate.format(DATE_FORMAT),
          terminationDate: currentDate.format(DATE_FORMAT),
          commentsAndAttachment: 'BBBBBB',
          contractTitle: 'BBBBBB',
          contractIdentifier: 'BBBBBB',
          contractIdentifierShort: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          terminationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ContractMetadata', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContractMetadataToCollectionIfMissing', () => {
      it('should add a ContractMetadata to an empty array', () => {
        const contractMetadata: IContractMetadata = { id: 123 };
        expectedResult = service.addContractMetadataToCollectionIfMissing([], contractMetadata);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractMetadata);
      });

      it('should not add a ContractMetadata to an array that contains it', () => {
        const contractMetadata: IContractMetadata = { id: 123 };
        const contractMetadataCollection: IContractMetadata[] = [
          {
            ...contractMetadata,
          },
          { id: 456 },
        ];
        expectedResult = service.addContractMetadataToCollectionIfMissing(contractMetadataCollection, contractMetadata);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContractMetadata to an array that doesn't contain it", () => {
        const contractMetadata: IContractMetadata = { id: 123 };
        const contractMetadataCollection: IContractMetadata[] = [{ id: 456 }];
        expectedResult = service.addContractMetadataToCollectionIfMissing(contractMetadataCollection, contractMetadata);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractMetadata);
      });

      it('should add only unique ContractMetadata to an array', () => {
        const contractMetadataArray: IContractMetadata[] = [{ id: 123 }, { id: 456 }, { id: 42677 }];
        const contractMetadataCollection: IContractMetadata[] = [{ id: 123 }];
        expectedResult = service.addContractMetadataToCollectionIfMissing(contractMetadataCollection, ...contractMetadataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractMetadata: IContractMetadata = { id: 123 };
        const contractMetadata2: IContractMetadata = { id: 456 };
        expectedResult = service.addContractMetadataToCollectionIfMissing([], contractMetadata, contractMetadata2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractMetadata);
        expect(expectedResult).toContain(contractMetadata2);
      });

      it('should accept null and undefined values', () => {
        const contractMetadata: IContractMetadata = { id: 123 };
        expectedResult = service.addContractMetadataToCollectionIfMissing([], null, contractMetadata, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractMetadata);
      });

      it('should return initial array if no ContractMetadata is added', () => {
        const contractMetadataCollection: IContractMetadata[] = [{ id: 123 }];
        expectedResult = service.addContractMetadataToCollectionIfMissing(contractMetadataCollection, undefined, null);
        expect(expectedResult).toEqual(contractMetadataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
