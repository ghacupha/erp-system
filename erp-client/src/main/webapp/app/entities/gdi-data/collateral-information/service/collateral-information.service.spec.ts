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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { CollateralInsuredFlagTypes } from 'app/entities/enumerations/collateral-insured-flag-types.model';
import { ICollateralInformation, CollateralInformation } from '../collateral-information.model';

import { CollateralInformationService } from './collateral-information.service';

describe('CollateralInformation Service', () => {
  let service: CollateralInformationService;
  let httpMock: HttpTestingController;
  let elemDefault: ICollateralInformation;
  let expectedResult: ICollateralInformation | ICollateralInformation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CollateralInformationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      collateralId: 'AAAAAAA',
      loanContractId: 'AAAAAAA',
      customerId: 'AAAAAAA',
      registrationPropertyNumber: 'AAAAAAA',
      collateralOMVInCCY: 0,
      collateralFSVInLCY: 0,
      collateralDiscountedValue: 0,
      amountCharged: 0,
      collateralDiscountRate: 0,
      loanToValueRatio: 0,
      nameOfPropertyValuer: 'AAAAAAA',
      collateralLastValuationDate: currentDate,
      insuredFlag: CollateralInsuredFlagTypes.Y,
      nameOfInsurer: 'AAAAAAA',
      amountInsured: 0,
      insuranceExpiryDate: currentDate,
      guaranteeInsurers: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          collateralLastValuationDate: currentDate.format(DATE_FORMAT),
          insuranceExpiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CollateralInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
          collateralLastValuationDate: currentDate.format(DATE_FORMAT),
          insuranceExpiryDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          collateralLastValuationDate: currentDate,
          insuranceExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CollateralInformation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CollateralInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          collateralId: 'BBBBBB',
          loanContractId: 'BBBBBB',
          customerId: 'BBBBBB',
          registrationPropertyNumber: 'BBBBBB',
          collateralOMVInCCY: 1,
          collateralFSVInLCY: 1,
          collateralDiscountedValue: 1,
          amountCharged: 1,
          collateralDiscountRate: 1,
          loanToValueRatio: 1,
          nameOfPropertyValuer: 'BBBBBB',
          collateralLastValuationDate: currentDate.format(DATE_FORMAT),
          insuredFlag: 'BBBBBB',
          nameOfInsurer: 'BBBBBB',
          amountInsured: 1,
          insuranceExpiryDate: currentDate.format(DATE_FORMAT),
          guaranteeInsurers: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          collateralLastValuationDate: currentDate,
          insuranceExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CollateralInformation', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          registrationPropertyNumber: 'BBBBBB',
          collateralOMVInCCY: 1,
          amountCharged: 1,
          loanToValueRatio: 1,
          nameOfPropertyValuer: 'BBBBBB',
          collateralLastValuationDate: currentDate.format(DATE_FORMAT),
          insuredFlag: 'BBBBBB',
          amountInsured: 1,
          guaranteeInsurers: 'BBBBBB',
        },
        new CollateralInformation()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          collateralLastValuationDate: currentDate,
          insuranceExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CollateralInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          collateralId: 'BBBBBB',
          loanContractId: 'BBBBBB',
          customerId: 'BBBBBB',
          registrationPropertyNumber: 'BBBBBB',
          collateralOMVInCCY: 1,
          collateralFSVInLCY: 1,
          collateralDiscountedValue: 1,
          amountCharged: 1,
          collateralDiscountRate: 1,
          loanToValueRatio: 1,
          nameOfPropertyValuer: 'BBBBBB',
          collateralLastValuationDate: currentDate.format(DATE_FORMAT),
          insuredFlag: 'BBBBBB',
          nameOfInsurer: 'BBBBBB',
          amountInsured: 1,
          insuranceExpiryDate: currentDate.format(DATE_FORMAT),
          guaranteeInsurers: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
          collateralLastValuationDate: currentDate,
          insuranceExpiryDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CollateralInformation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCollateralInformationToCollectionIfMissing', () => {
      it('should add a CollateralInformation to an empty array', () => {
        const collateralInformation: ICollateralInformation = { id: 123 };
        expectedResult = service.addCollateralInformationToCollectionIfMissing([], collateralInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collateralInformation);
      });

      it('should not add a CollateralInformation to an array that contains it', () => {
        const collateralInformation: ICollateralInformation = { id: 123 };
        const collateralInformationCollection: ICollateralInformation[] = [
          {
            ...collateralInformation,
          },
          { id: 456 },
        ];
        expectedResult = service.addCollateralInformationToCollectionIfMissing(collateralInformationCollection, collateralInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CollateralInformation to an array that doesn't contain it", () => {
        const collateralInformation: ICollateralInformation = { id: 123 };
        const collateralInformationCollection: ICollateralInformation[] = [{ id: 456 }];
        expectedResult = service.addCollateralInformationToCollectionIfMissing(collateralInformationCollection, collateralInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collateralInformation);
      });

      it('should add only unique CollateralInformation to an array', () => {
        const collateralInformationArray: ICollateralInformation[] = [{ id: 123 }, { id: 456 }, { id: 92602 }];
        const collateralInformationCollection: ICollateralInformation[] = [{ id: 123 }];
        expectedResult = service.addCollateralInformationToCollectionIfMissing(
          collateralInformationCollection,
          ...collateralInformationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const collateralInformation: ICollateralInformation = { id: 123 };
        const collateralInformation2: ICollateralInformation = { id: 456 };
        expectedResult = service.addCollateralInformationToCollectionIfMissing([], collateralInformation, collateralInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collateralInformation);
        expect(expectedResult).toContain(collateralInformation2);
      });

      it('should accept null and undefined values', () => {
        const collateralInformation: ICollateralInformation = { id: 123 };
        expectedResult = service.addCollateralInformationToCollectionIfMissing([], null, collateralInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collateralInformation);
      });

      it('should return initial array if no CollateralInformation is added', () => {
        const collateralInformationCollection: ICollateralInformation[] = [{ id: 123 }];
        expectedResult = service.addCollateralInformationToCollectionIfMissing(collateralInformationCollection, undefined, null);
        expect(expectedResult).toEqual(collateralInformationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
