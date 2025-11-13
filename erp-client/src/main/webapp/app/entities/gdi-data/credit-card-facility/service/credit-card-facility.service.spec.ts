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
import { ICreditCardFacility, CreditCardFacility } from '../credit-card-facility.model';

import { CreditCardFacilityService } from './credit-card-facility.service';

describe('CreditCardFacility Service', () => {
  let service: CreditCardFacilityService;
  let httpMock: HttpTestingController;
  let elemDefault: ICreditCardFacility;
  let expectedResult: ICreditCardFacility | ICreditCardFacility[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CreditCardFacilityService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      totalNumberOfActiveCreditCards: 0,
      totalCreditCardLimitsInCCY: 0,
      totalCreditCardLimitsInLCY: 0,
      totalCreditCardAmountUtilisedInCCY: 0,
      totalCreditCardAmountUtilisedInLcy: 0,
      totalNPACreditCardAmountInFCY: 0,
      totalNPACreditCardAmountInLCY: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CreditCardFacility', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CreditCardFacility()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CreditCardFacility', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfActiveCreditCards: 1,
          totalCreditCardLimitsInCCY: 1,
          totalCreditCardLimitsInLCY: 1,
          totalCreditCardAmountUtilisedInCCY: 1,
          totalCreditCardAmountUtilisedInLcy: 1,
          totalNPACreditCardAmountInFCY: 1,
          totalNPACreditCardAmountInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CreditCardFacility', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfActiveCreditCards: 1,
          totalNPACreditCardAmountInLCY: 1,
        },
        new CreditCardFacility()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CreditCardFacility', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          totalNumberOfActiveCreditCards: 1,
          totalCreditCardLimitsInCCY: 1,
          totalCreditCardLimitsInLCY: 1,
          totalCreditCardAmountUtilisedInCCY: 1,
          totalCreditCardAmountUtilisedInLcy: 1,
          totalNPACreditCardAmountInFCY: 1,
          totalNPACreditCardAmountInLCY: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CreditCardFacility', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCreditCardFacilityToCollectionIfMissing', () => {
      it('should add a CreditCardFacility to an empty array', () => {
        const creditCardFacility: ICreditCardFacility = { id: 123 };
        expectedResult = service.addCreditCardFacilityToCollectionIfMissing([], creditCardFacility);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditCardFacility);
      });

      it('should not add a CreditCardFacility to an array that contains it', () => {
        const creditCardFacility: ICreditCardFacility = { id: 123 };
        const creditCardFacilityCollection: ICreditCardFacility[] = [
          {
            ...creditCardFacility,
          },
          { id: 456 },
        ];
        expectedResult = service.addCreditCardFacilityToCollectionIfMissing(creditCardFacilityCollection, creditCardFacility);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CreditCardFacility to an array that doesn't contain it", () => {
        const creditCardFacility: ICreditCardFacility = { id: 123 };
        const creditCardFacilityCollection: ICreditCardFacility[] = [{ id: 456 }];
        expectedResult = service.addCreditCardFacilityToCollectionIfMissing(creditCardFacilityCollection, creditCardFacility);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditCardFacility);
      });

      it('should add only unique CreditCardFacility to an array', () => {
        const creditCardFacilityArray: ICreditCardFacility[] = [{ id: 123 }, { id: 456 }, { id: 91025 }];
        const creditCardFacilityCollection: ICreditCardFacility[] = [{ id: 123 }];
        expectedResult = service.addCreditCardFacilityToCollectionIfMissing(creditCardFacilityCollection, ...creditCardFacilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const creditCardFacility: ICreditCardFacility = { id: 123 };
        const creditCardFacility2: ICreditCardFacility = { id: 456 };
        expectedResult = service.addCreditCardFacilityToCollectionIfMissing([], creditCardFacility, creditCardFacility2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditCardFacility);
        expect(expectedResult).toContain(creditCardFacility2);
      });

      it('should accept null and undefined values', () => {
        const creditCardFacility: ICreditCardFacility = { id: 123 };
        expectedResult = service.addCreditCardFacilityToCollectionIfMissing([], null, creditCardFacility, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditCardFacility);
      });

      it('should return initial array if no CreditCardFacility is added', () => {
        const creditCardFacilityCollection: ICreditCardFacility[] = [{ id: 123 }];
        expectedResult = service.addCreditCardFacilityToCollectionIfMissing(creditCardFacilityCollection, undefined, null);
        expect(expectedResult).toEqual(creditCardFacilityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
