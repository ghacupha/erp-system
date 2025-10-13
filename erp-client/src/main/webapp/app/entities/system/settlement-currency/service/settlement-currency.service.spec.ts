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

import { ISettlementCurrency, SettlementCurrency } from '../settlement-currency.model';

import { SettlementCurrencyService } from './settlement-currency.service';

describe('SettlementCurrency Service', () => {
  let service: SettlementCurrencyService;
  let httpMock: HttpTestingController;
  let elemDefault: ISettlementCurrency;
  let expectedResult: ISettlementCurrency | ISettlementCurrency[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SettlementCurrencyService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      iso4217CurrencyCode: 'AAAAAAA',
      currencyName: 'AAAAAAA',
      country: 'AAAAAAA',
      numericCode: 'AAAAAAA',
      minorUnit: 'AAAAAAA',
      fileUploadToken: 'AAAAAAA',
      compilationToken: 'AAAAAAA',
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

    it('should create a SettlementCurrency', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SettlementCurrency()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SettlementCurrency', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iso4217CurrencyCode: 'BBBBBB',
          currencyName: 'BBBBBB',
          country: 'BBBBBB',
          numericCode: 'BBBBBB',
          minorUnit: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SettlementCurrency', () => {
      const patchObject = Object.assign(
        {
          numericCode: 'BBBBBB',
        },
        new SettlementCurrency()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SettlementCurrency', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          iso4217CurrencyCode: 'BBBBBB',
          currencyName: 'BBBBBB',
          country: 'BBBBBB',
          numericCode: 'BBBBBB',
          minorUnit: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
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

    it('should delete a SettlementCurrency', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSettlementCurrencyToCollectionIfMissing', () => {
      it('should add a SettlementCurrency to an empty array', () => {
        const settlementCurrency: ISettlementCurrency = { id: 123 };
        expectedResult = service.addSettlementCurrencyToCollectionIfMissing([], settlementCurrency);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settlementCurrency);
      });

      it('should not add a SettlementCurrency to an array that contains it', () => {
        const settlementCurrency: ISettlementCurrency = { id: 123 };
        const settlementCurrencyCollection: ISettlementCurrency[] = [
          {
            ...settlementCurrency,
          },
          { id: 456 },
        ];
        expectedResult = service.addSettlementCurrencyToCollectionIfMissing(settlementCurrencyCollection, settlementCurrency);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SettlementCurrency to an array that doesn't contain it", () => {
        const settlementCurrency: ISettlementCurrency = { id: 123 };
        const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 456 }];
        expectedResult = service.addSettlementCurrencyToCollectionIfMissing(settlementCurrencyCollection, settlementCurrency);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settlementCurrency);
      });

      it('should add only unique SettlementCurrency to an array', () => {
        const settlementCurrencyArray: ISettlementCurrency[] = [{ id: 123 }, { id: 456 }, { id: 37004 }];
        const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 123 }];
        expectedResult = service.addSettlementCurrencyToCollectionIfMissing(settlementCurrencyCollection, ...settlementCurrencyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const settlementCurrency: ISettlementCurrency = { id: 123 };
        const settlementCurrency2: ISettlementCurrency = { id: 456 };
        expectedResult = service.addSettlementCurrencyToCollectionIfMissing([], settlementCurrency, settlementCurrency2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settlementCurrency);
        expect(expectedResult).toContain(settlementCurrency2);
      });

      it('should accept null and undefined values', () => {
        const settlementCurrency: ISettlementCurrency = { id: 123 };
        expectedResult = service.addSettlementCurrencyToCollectionIfMissing([], null, settlementCurrency, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settlementCurrency);
      });

      it('should return initial array if no SettlementCurrency is added', () => {
        const settlementCurrencyCollection: ISettlementCurrency[] = [{ id: 123 }];
        expectedResult = service.addSettlementCurrencyToCollectionIfMissing(settlementCurrencyCollection, undefined, null);
        expect(expectedResult).toEqual(settlementCurrencyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
