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

import { ShareHolderTypes } from 'app/entities/enumerations/share-holder-types.model';
import { IShareholderType, ShareholderType } from '../shareholder-type.model';

import { ShareholderTypeService } from './shareholder-type.service';

describe('ShareholderType Service', () => {
  let service: ShareholderTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IShareholderType;
  let expectedResult: IShareholderType | IShareholderType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShareholderTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      shareHolderTypeCode: 'AAAAAAA',
      shareHolderType: ShareHolderTypes.INDIVIDUAL,
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

    it('should create a ShareholderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ShareholderType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShareholderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shareHolderTypeCode: 'BBBBBB',
          shareHolderType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShareholderType', () => {
      const patchObject = Object.assign(
        {
          shareHolderTypeCode: 'BBBBBB',
          shareHolderType: 'BBBBBB',
        },
        new ShareholderType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShareholderType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shareHolderTypeCode: 'BBBBBB',
          shareHolderType: 'BBBBBB',
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

    it('should delete a ShareholderType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addShareholderTypeToCollectionIfMissing', () => {
      it('should add a ShareholderType to an empty array', () => {
        const shareholderType: IShareholderType = { id: 123 };
        expectedResult = service.addShareholderTypeToCollectionIfMissing([], shareholderType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shareholderType);
      });

      it('should not add a ShareholderType to an array that contains it', () => {
        const shareholderType: IShareholderType = { id: 123 };
        const shareholderTypeCollection: IShareholderType[] = [
          {
            ...shareholderType,
          },
          { id: 456 },
        ];
        expectedResult = service.addShareholderTypeToCollectionIfMissing(shareholderTypeCollection, shareholderType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShareholderType to an array that doesn't contain it", () => {
        const shareholderType: IShareholderType = { id: 123 };
        const shareholderTypeCollection: IShareholderType[] = [{ id: 456 }];
        expectedResult = service.addShareholderTypeToCollectionIfMissing(shareholderTypeCollection, shareholderType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shareholderType);
      });

      it('should add only unique ShareholderType to an array', () => {
        const shareholderTypeArray: IShareholderType[] = [{ id: 123 }, { id: 456 }, { id: 94079 }];
        const shareholderTypeCollection: IShareholderType[] = [{ id: 123 }];
        expectedResult = service.addShareholderTypeToCollectionIfMissing(shareholderTypeCollection, ...shareholderTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shareholderType: IShareholderType = { id: 123 };
        const shareholderType2: IShareholderType = { id: 456 };
        expectedResult = service.addShareholderTypeToCollectionIfMissing([], shareholderType, shareholderType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shareholderType);
        expect(expectedResult).toContain(shareholderType2);
      });

      it('should accept null and undefined values', () => {
        const shareholderType: IShareholderType = { id: 123 };
        expectedResult = service.addShareholderTypeToCollectionIfMissing([], null, shareholderType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shareholderType);
      });

      it('should return initial array if no ShareholderType is added', () => {
        const shareholderTypeCollection: IShareholderType[] = [{ id: 123 }];
        expectedResult = service.addShareholderTypeToCollectionIfMissing(shareholderTypeCollection, undefined, null);
        expect(expectedResult).toEqual(shareholderTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
