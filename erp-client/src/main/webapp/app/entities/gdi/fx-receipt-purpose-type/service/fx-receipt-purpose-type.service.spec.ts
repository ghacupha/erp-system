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

import { IFxReceiptPurposeType, FxReceiptPurposeType } from '../fx-receipt-purpose-type.model';

import { FxReceiptPurposeTypeService } from './fx-receipt-purpose-type.service';

describe('FxReceiptPurposeType Service', () => {
  let service: FxReceiptPurposeTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFxReceiptPurposeType;
  let expectedResult: IFxReceiptPurposeType | IFxReceiptPurposeType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FxReceiptPurposeTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      itemCode: 'AAAAAAA',
      attribute1ReceiptPaymentPurposeCode: 'AAAAAAA',
      attribute1ReceiptPaymentPurposeType: 'AAAAAAA',
      attribute2ReceiptPaymentPurposeCode: 'AAAAAAA',
      attribute2ReceiptPaymentPurposeDescription: 'AAAAAAA',
      attribute3ReceiptPaymentPurposeCode: 'AAAAAAA',
      attribute3ReceiptPaymentPurposeDescription: 'AAAAAAA',
      attribute4ReceiptPaymentPurposeCode: 'AAAAAAA',
      attribute4ReceiptPaymentPurposeDescription: 'AAAAAAA',
      attribute5ReceiptPaymentPurposeCode: 'AAAAAAA',
      attribute5ReceiptPaymentPurposeDescription: 'AAAAAAA',
      lastChild: 'AAAAAAA',
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

    it('should create a FxReceiptPurposeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FxReceiptPurposeType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FxReceiptPurposeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          itemCode: 'BBBBBB',
          attribute1ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute1ReceiptPaymentPurposeType: 'BBBBBB',
          attribute2ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute2ReceiptPaymentPurposeDescription: 'BBBBBB',
          attribute3ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute3ReceiptPaymentPurposeDescription: 'BBBBBB',
          attribute4ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute4ReceiptPaymentPurposeDescription: 'BBBBBB',
          attribute5ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute5ReceiptPaymentPurposeDescription: 'BBBBBB',
          lastChild: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FxReceiptPurposeType', () => {
      const patchObject = Object.assign(
        {
          attribute1ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute3ReceiptPaymentPurposeCode: 'BBBBBB',
          lastChild: 'BBBBBB',
        },
        new FxReceiptPurposeType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FxReceiptPurposeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          itemCode: 'BBBBBB',
          attribute1ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute1ReceiptPaymentPurposeType: 'BBBBBB',
          attribute2ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute2ReceiptPaymentPurposeDescription: 'BBBBBB',
          attribute3ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute3ReceiptPaymentPurposeDescription: 'BBBBBB',
          attribute4ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute4ReceiptPaymentPurposeDescription: 'BBBBBB',
          attribute5ReceiptPaymentPurposeCode: 'BBBBBB',
          attribute5ReceiptPaymentPurposeDescription: 'BBBBBB',
          lastChild: 'BBBBBB',
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

    it('should delete a FxReceiptPurposeType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFxReceiptPurposeTypeToCollectionIfMissing', () => {
      it('should add a FxReceiptPurposeType to an empty array', () => {
        const fxReceiptPurposeType: IFxReceiptPurposeType = { id: 123 };
        expectedResult = service.addFxReceiptPurposeTypeToCollectionIfMissing([], fxReceiptPurposeType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxReceiptPurposeType);
      });

      it('should not add a FxReceiptPurposeType to an array that contains it', () => {
        const fxReceiptPurposeType: IFxReceiptPurposeType = { id: 123 };
        const fxReceiptPurposeTypeCollection: IFxReceiptPurposeType[] = [
          {
            ...fxReceiptPurposeType,
          },
          { id: 456 },
        ];
        expectedResult = service.addFxReceiptPurposeTypeToCollectionIfMissing(fxReceiptPurposeTypeCollection, fxReceiptPurposeType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FxReceiptPurposeType to an array that doesn't contain it", () => {
        const fxReceiptPurposeType: IFxReceiptPurposeType = { id: 123 };
        const fxReceiptPurposeTypeCollection: IFxReceiptPurposeType[] = [{ id: 456 }];
        expectedResult = service.addFxReceiptPurposeTypeToCollectionIfMissing(fxReceiptPurposeTypeCollection, fxReceiptPurposeType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxReceiptPurposeType);
      });

      it('should add only unique FxReceiptPurposeType to an array', () => {
        const fxReceiptPurposeTypeArray: IFxReceiptPurposeType[] = [{ id: 123 }, { id: 456 }, { id: 37000 }];
        const fxReceiptPurposeTypeCollection: IFxReceiptPurposeType[] = [{ id: 123 }];
        expectedResult = service.addFxReceiptPurposeTypeToCollectionIfMissing(fxReceiptPurposeTypeCollection, ...fxReceiptPurposeTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fxReceiptPurposeType: IFxReceiptPurposeType = { id: 123 };
        const fxReceiptPurposeType2: IFxReceiptPurposeType = { id: 456 };
        expectedResult = service.addFxReceiptPurposeTypeToCollectionIfMissing([], fxReceiptPurposeType, fxReceiptPurposeType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fxReceiptPurposeType);
        expect(expectedResult).toContain(fxReceiptPurposeType2);
      });

      it('should accept null and undefined values', () => {
        const fxReceiptPurposeType: IFxReceiptPurposeType = { id: 123 };
        expectedResult = service.addFxReceiptPurposeTypeToCollectionIfMissing([], null, fxReceiptPurposeType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fxReceiptPurposeType);
      });

      it('should return initial array if no FxReceiptPurposeType is added', () => {
        const fxReceiptPurposeTypeCollection: IFxReceiptPurposeType[] = [{ id: 123 }];
        expectedResult = service.addFxReceiptPurposeTypeToCollectionIfMissing(fxReceiptPurposeTypeCollection, undefined, null);
        expect(expectedResult).toEqual(fxReceiptPurposeTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
