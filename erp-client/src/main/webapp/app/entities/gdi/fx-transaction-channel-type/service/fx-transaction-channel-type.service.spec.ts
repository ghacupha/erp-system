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

import { IFxTransactionChannelType, FxTransactionChannelType } from '../fx-transaction-channel-type.model';

import { FxTransactionChannelTypeService } from './fx-transaction-channel-type.service';

describe('FxTransactionChannelType Service', () => {
  let service: FxTransactionChannelTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFxTransactionChannelType;
  let expectedResult: IFxTransactionChannelType | IFxTransactionChannelType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FxTransactionChannelTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fxTransactionChannelCode: 'AAAAAAA',
      fxTransactionChannelType: 'AAAAAAA',
      fxChannelTypeDetails: 'AAAAAAA',
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

    it('should create a FxTransactionChannelType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FxTransactionChannelType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FxTransactionChannelType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxTransactionChannelCode: 'BBBBBB',
          fxTransactionChannelType: 'BBBBBB',
          fxChannelTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FxTransactionChannelType', () => {
      const patchObject = Object.assign({}, new FxTransactionChannelType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FxTransactionChannelType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fxTransactionChannelCode: 'BBBBBB',
          fxTransactionChannelType: 'BBBBBB',
          fxChannelTypeDetails: 'BBBBBB',
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

    it('should delete a FxTransactionChannelType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFxTransactionChannelTypeToCollectionIfMissing', () => {
      it('should add a FxTransactionChannelType to an empty array', () => {
        const fxTransactionChannelType: IFxTransactionChannelType = { id: 123 };
        expectedResult = service.addFxTransactionChannelTypeToCollectionIfMissing([], fxTransactionChannelType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxTransactionChannelType);
      });

      it('should not add a FxTransactionChannelType to an array that contains it', () => {
        const fxTransactionChannelType: IFxTransactionChannelType = { id: 123 };
        const fxTransactionChannelTypeCollection: IFxTransactionChannelType[] = [
          {
            ...fxTransactionChannelType,
          },
          { id: 456 },
        ];
        expectedResult = service.addFxTransactionChannelTypeToCollectionIfMissing(
          fxTransactionChannelTypeCollection,
          fxTransactionChannelType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FxTransactionChannelType to an array that doesn't contain it", () => {
        const fxTransactionChannelType: IFxTransactionChannelType = { id: 123 };
        const fxTransactionChannelTypeCollection: IFxTransactionChannelType[] = [{ id: 456 }];
        expectedResult = service.addFxTransactionChannelTypeToCollectionIfMissing(
          fxTransactionChannelTypeCollection,
          fxTransactionChannelType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxTransactionChannelType);
      });

      it('should add only unique FxTransactionChannelType to an array', () => {
        const fxTransactionChannelTypeArray: IFxTransactionChannelType[] = [{ id: 123 }, { id: 456 }, { id: 43707 }];
        const fxTransactionChannelTypeCollection: IFxTransactionChannelType[] = [{ id: 123 }];
        expectedResult = service.addFxTransactionChannelTypeToCollectionIfMissing(
          fxTransactionChannelTypeCollection,
          ...fxTransactionChannelTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fxTransactionChannelType: IFxTransactionChannelType = { id: 123 };
        const fxTransactionChannelType2: IFxTransactionChannelType = { id: 456 };
        expectedResult = service.addFxTransactionChannelTypeToCollectionIfMissing([], fxTransactionChannelType, fxTransactionChannelType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxTransactionChannelType);
        expect(expectedResult).toContain(fxTransactionChannelType2);
      });

      it('should accept null and undefined values', () => {
        const fxTransactionChannelType: IFxTransactionChannelType = { id: 123 };
        expectedResult = service.addFxTransactionChannelTypeToCollectionIfMissing([], null, fxTransactionChannelType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxTransactionChannelType);
      });

      it('should return initial array if no FxTransactionChannelType is added', () => {
        const fxTransactionChannelTypeCollection: IFxTransactionChannelType[] = [{ id: 123 }];
        expectedResult = service.addFxTransactionChannelTypeToCollectionIfMissing(fxTransactionChannelTypeCollection, undefined, null);
        expect(expectedResult).toEqual(fxTransactionChannelTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
