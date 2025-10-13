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

import { IKenyanCurrencyDenomination, KenyanCurrencyDenomination } from '../kenyan-currency-denomination.model';

import { KenyanCurrencyDenominationService } from './kenyan-currency-denomination.service';

describe('KenyanCurrencyDenomination Service', () => {
  let service: KenyanCurrencyDenominationService;
  let httpMock: HttpTestingController;
  let elemDefault: IKenyanCurrencyDenomination;
  let expectedResult: IKenyanCurrencyDenomination | IKenyanCurrencyDenomination[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(KenyanCurrencyDenominationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      currencyDenominationCode: 'AAAAAAA',
      currencyDenominationType: 'AAAAAAA',
      currencyDenominationTypeDetails: 'AAAAAAA',
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

    it('should create a KenyanCurrencyDenomination', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new KenyanCurrencyDenomination()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KenyanCurrencyDenomination', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currencyDenominationCode: 'BBBBBB',
          currencyDenominationType: 'BBBBBB',
          currencyDenominationTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KenyanCurrencyDenomination', () => {
      const patchObject = Object.assign(
        {
          currencyDenominationTypeDetails: 'BBBBBB',
        },
        new KenyanCurrencyDenomination()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KenyanCurrencyDenomination', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currencyDenominationCode: 'BBBBBB',
          currencyDenominationType: 'BBBBBB',
          currencyDenominationTypeDetails: 'BBBBBB',
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

    it('should delete a KenyanCurrencyDenomination', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addKenyanCurrencyDenominationToCollectionIfMissing', () => {
      it('should add a KenyanCurrencyDenomination to an empty array', () => {
        const kenyanCurrencyDenomination: IKenyanCurrencyDenomination = { id: 123 };
        expectedResult = service.addKenyanCurrencyDenominationToCollectionIfMissing([], kenyanCurrencyDenomination);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kenyanCurrencyDenomination);
      });

      it('should not add a KenyanCurrencyDenomination to an array that contains it', () => {
        const kenyanCurrencyDenomination: IKenyanCurrencyDenomination = { id: 123 };
        const kenyanCurrencyDenominationCollection: IKenyanCurrencyDenomination[] = [
          {
            ...kenyanCurrencyDenomination,
          },
          { id: 456 },
        ];
        expectedResult = service.addKenyanCurrencyDenominationToCollectionIfMissing(
          kenyanCurrencyDenominationCollection,
          kenyanCurrencyDenomination
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KenyanCurrencyDenomination to an array that doesn't contain it", () => {
        const kenyanCurrencyDenomination: IKenyanCurrencyDenomination = { id: 123 };
        const kenyanCurrencyDenominationCollection: IKenyanCurrencyDenomination[] = [{ id: 456 }];
        expectedResult = service.addKenyanCurrencyDenominationToCollectionIfMissing(
          kenyanCurrencyDenominationCollection,
          kenyanCurrencyDenomination
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kenyanCurrencyDenomination);
      });

      it('should add only unique KenyanCurrencyDenomination to an array', () => {
        const kenyanCurrencyDenominationArray: IKenyanCurrencyDenomination[] = [{ id: 123 }, { id: 456 }, { id: 50315 }];
        const kenyanCurrencyDenominationCollection: IKenyanCurrencyDenomination[] = [{ id: 123 }];
        expectedResult = service.addKenyanCurrencyDenominationToCollectionIfMissing(
          kenyanCurrencyDenominationCollection,
          ...kenyanCurrencyDenominationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kenyanCurrencyDenomination: IKenyanCurrencyDenomination = { id: 123 };
        const kenyanCurrencyDenomination2: IKenyanCurrencyDenomination = { id: 456 };
        expectedResult = service.addKenyanCurrencyDenominationToCollectionIfMissing(
          [],
          kenyanCurrencyDenomination,
          kenyanCurrencyDenomination2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kenyanCurrencyDenomination);
        expect(expectedResult).toContain(kenyanCurrencyDenomination2);
      });

      it('should accept null and undefined values', () => {
        const kenyanCurrencyDenomination: IKenyanCurrencyDenomination = { id: 123 };
        expectedResult = service.addKenyanCurrencyDenominationToCollectionIfMissing([], null, kenyanCurrencyDenomination, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kenyanCurrencyDenomination);
      });

      it('should return initial array if no KenyanCurrencyDenomination is added', () => {
        const kenyanCurrencyDenominationCollection: IKenyanCurrencyDenomination[] = [{ id: 123 }];
        expectedResult = service.addKenyanCurrencyDenominationToCollectionIfMissing(kenyanCurrencyDenominationCollection, undefined, null);
        expect(expectedResult).toEqual(kenyanCurrencyDenominationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
