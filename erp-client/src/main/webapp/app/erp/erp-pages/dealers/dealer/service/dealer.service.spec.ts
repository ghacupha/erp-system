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
import { DealerService } from './dealer.service';
import { Dealer, IDealer } from '../dealer.model';
import { ErpCommonModule } from '../../../../erp-common/erp-common.module';

describe('Service Tests', () => {
  describe('Dealer Service', () => {
    let service: DealerService;
    let httpMock: HttpTestingController;
    let elemDefault: IDealer;
    let expectedResult: IDealer | IDealer[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpCommonModule, HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DealerService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        dealerName: 'AAAAAAA',
        taxNumber: 'AAAAAAA',
        postalAddress: 'AAAAAAA',
        physicalAddress: 'AAAAAAA',
        accountName: 'AAAAAAA',
        accountNumber: 'AAAAAAA',
        bankersName: 'AAAAAAA',
        bankersBranch: 'AAAAAAA',
        bankersSwiftCode: 'AAAAAAA',
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

      it('should create a Dealer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Dealer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dealer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dealerName: 'BBBBBB',
            taxNumber: 'BBBBBB',
            postalAddress: 'BBBBBB',
            physicalAddress: 'BBBBBB',
            accountName: 'BBBBBB',
            accountNumber: 'BBBBBB',
            bankersName: 'BBBBBB',
            bankersBranch: 'BBBBBB',
            bankersSwiftCode: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Dealer', () => {
        const patchObject = Object.assign(
          {
            dealerName: 'BBBBBB',
            postalAddress: 'BBBBBB',
            physicalAddress: 'BBBBBB',
            accountName: 'BBBBBB',
            bankersName: 'BBBBBB',
          },
          new Dealer()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dealer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dealerName: 'BBBBBB',
            taxNumber: 'BBBBBB',
            postalAddress: 'BBBBBB',
            physicalAddress: 'BBBBBB',
            accountName: 'BBBBBB',
            accountNumber: 'BBBBBB',
            bankersName: 'BBBBBB',
            bankersBranch: 'BBBBBB',
            bankersSwiftCode: 'BBBBBB',
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

      it('should delete a Dealer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDealerToCollectionIfMissing', () => {
        it('should add a Dealer to an empty array', () => {
          const dealer: IDealer = { id: 123 };
          expectedResult = service.addDealerToCollectionIfMissing([], dealer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dealer);
        });

        it('should not add a Dealer to an array that contains it', () => {
          const dealer: IDealer = { id: 123 };
          const dealerCollection: IDealer[] = [
            {
              ...dealer,
            },
            { id: 456 },
          ];
          expectedResult = service.addDealerToCollectionIfMissing(dealerCollection, dealer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Dealer to an array that doesn't contain it", () => {
          const dealer: IDealer = { id: 123 };
          const dealerCollection: IDealer[] = [{ id: 456 }];
          expectedResult = service.addDealerToCollectionIfMissing(dealerCollection, dealer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dealer);
        });

        it('should add only unique Dealer to an array', () => {
          const dealerArray: IDealer[] = [{ id: 123 }, { id: 456 }, { id: 39180 }];
          const dealerCollection: IDealer[] = [{ id: 123 }];
          expectedResult = service.addDealerToCollectionIfMissing(dealerCollection, ...dealerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dealer: IDealer = { id: 123 };
          const dealer2: IDealer = { id: 456 };
          expectedResult = service.addDealerToCollectionIfMissing([], dealer, dealer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dealer);
          expect(expectedResult).toContain(dealer2);
        });

        it('should accept null and undefined values', () => {
          const dealer: IDealer = { id: 123 };
          expectedResult = service.addDealerToCollectionIfMissing([], null, dealer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dealer);
        });

        it('should return initial array if no Dealer is added', () => {
          const dealerCollection: IDealer[] = [{ id: 123 }];
          expectedResult = service.addDealerToCollectionIfMissing(dealerCollection, undefined, null);
          expect(expectedResult).toEqual(dealerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
