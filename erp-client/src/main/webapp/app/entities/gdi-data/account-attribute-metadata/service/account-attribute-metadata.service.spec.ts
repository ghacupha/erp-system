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

import { MandatoryFieldFlagTypes } from 'app/entities/enumerations/mandatory-field-flag-types.model';
import { IAccountAttributeMetadata, AccountAttributeMetadata } from '../account-attribute-metadata.model';

import { AccountAttributeMetadataService } from './account-attribute-metadata.service';

describe('AccountAttributeMetadata Service', () => {
  let service: AccountAttributeMetadataService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountAttributeMetadata;
  let expectedResult: IAccountAttributeMetadata | IAccountAttributeMetadata[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountAttributeMetadataService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      precedence: 0,
      columnName: 'AAAAAAA',
      shortName: 'AAAAAAA',
      detailedDefinition: 'AAAAAAA',
      dataType: 'AAAAAAA',
      length: 0,
      columnIndex: 'AAAAAAA',
      mandatoryFieldFlag: MandatoryFieldFlagTypes.Y,
      businessValidation: 'AAAAAAA',
      technicalValidation: 'AAAAAAA',
      dbColumnName: 'AAAAAAA',
      metadataVersion: 0,
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

    it('should create a AccountAttributeMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AccountAttributeMetadata()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountAttributeMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          precedence: 1,
          columnName: 'BBBBBB',
          shortName: 'BBBBBB',
          detailedDefinition: 'BBBBBB',
          dataType: 'BBBBBB',
          length: 1,
          columnIndex: 'BBBBBB',
          mandatoryFieldFlag: 'BBBBBB',
          businessValidation: 'BBBBBB',
          technicalValidation: 'BBBBBB',
          dbColumnName: 'BBBBBB',
          metadataVersion: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountAttributeMetadata', () => {
      const patchObject = Object.assign(
        {
          columnName: 'BBBBBB',
          shortName: 'BBBBBB',
          dataType: 'BBBBBB',
          columnIndex: 'BBBBBB',
          technicalValidation: 'BBBBBB',
        },
        new AccountAttributeMetadata()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountAttributeMetadata', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          precedence: 1,
          columnName: 'BBBBBB',
          shortName: 'BBBBBB',
          detailedDefinition: 'BBBBBB',
          dataType: 'BBBBBB',
          length: 1,
          columnIndex: 'BBBBBB',
          mandatoryFieldFlag: 'BBBBBB',
          businessValidation: 'BBBBBB',
          technicalValidation: 'BBBBBB',
          dbColumnName: 'BBBBBB',
          metadataVersion: 1,
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

    it('should delete a AccountAttributeMetadata', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccountAttributeMetadataToCollectionIfMissing', () => {
      it('should add a AccountAttributeMetadata to an empty array', () => {
        const accountAttributeMetadata: IAccountAttributeMetadata = { id: 123 };
        expectedResult = service.addAccountAttributeMetadataToCollectionIfMissing([], accountAttributeMetadata);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountAttributeMetadata);
      });

      it('should not add a AccountAttributeMetadata to an array that contains it', () => {
        const accountAttributeMetadata: IAccountAttributeMetadata = { id: 123 };
        const accountAttributeMetadataCollection: IAccountAttributeMetadata[] = [
          {
            ...accountAttributeMetadata,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountAttributeMetadataToCollectionIfMissing(
          accountAttributeMetadataCollection,
          accountAttributeMetadata
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountAttributeMetadata to an array that doesn't contain it", () => {
        const accountAttributeMetadata: IAccountAttributeMetadata = { id: 123 };
        const accountAttributeMetadataCollection: IAccountAttributeMetadata[] = [{ id: 456 }];
        expectedResult = service.addAccountAttributeMetadataToCollectionIfMissing(
          accountAttributeMetadataCollection,
          accountAttributeMetadata
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountAttributeMetadata);
      });

      it('should add only unique AccountAttributeMetadata to an array', () => {
        const accountAttributeMetadataArray: IAccountAttributeMetadata[] = [{ id: 123 }, { id: 456 }, { id: 36965 }];
        const accountAttributeMetadataCollection: IAccountAttributeMetadata[] = [{ id: 123 }];
        expectedResult = service.addAccountAttributeMetadataToCollectionIfMissing(
          accountAttributeMetadataCollection,
          ...accountAttributeMetadataArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountAttributeMetadata: IAccountAttributeMetadata = { id: 123 };
        const accountAttributeMetadata2: IAccountAttributeMetadata = { id: 456 };
        expectedResult = service.addAccountAttributeMetadataToCollectionIfMissing([], accountAttributeMetadata, accountAttributeMetadata2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountAttributeMetadata);
        expect(expectedResult).toContain(accountAttributeMetadata2);
      });

      it('should accept null and undefined values', () => {
        const accountAttributeMetadata: IAccountAttributeMetadata = { id: 123 };
        expectedResult = service.addAccountAttributeMetadataToCollectionIfMissing([], null, accountAttributeMetadata, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountAttributeMetadata);
      });

      it('should return initial array if no AccountAttributeMetadata is added', () => {
        const accountAttributeMetadataCollection: IAccountAttributeMetadata[] = [{ id: 123 }];
        expectedResult = service.addAccountAttributeMetadataToCollectionIfMissing(accountAttributeMetadataCollection, undefined, null);
        expect(expectedResult).toEqual(accountAttributeMetadataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
