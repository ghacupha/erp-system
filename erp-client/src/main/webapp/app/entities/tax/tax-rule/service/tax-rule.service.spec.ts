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

import { ITaxRule, TaxRule } from '../tax-rule.model';

import { TaxRuleService } from './tax-rule.service';

describe('TaxRule Service', () => {
  let service: TaxRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ITaxRule;
  let expectedResult: ITaxRule | ITaxRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TaxRuleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      telcoExciseDuty: 0,
      valueAddedTax: 0,
      withholdingVAT: 0,
      withholdingTaxConsultancy: 0,
      withholdingTaxRent: 0,
      cateringLevy: 0,
      serviceCharge: 0,
      withholdingTaxImportedService: 0,
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

    it('should create a TaxRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TaxRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TaxRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          telcoExciseDuty: 1,
          valueAddedTax: 1,
          withholdingVAT: 1,
          withholdingTaxConsultancy: 1,
          withholdingTaxRent: 1,
          cateringLevy: 1,
          serviceCharge: 1,
          withholdingTaxImportedService: 1,
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

    it('should partial update a TaxRule', () => {
      const patchObject = Object.assign(
        {
          valueAddedTax: 1,
          withholdingTaxConsultancy: 1,
          withholdingTaxRent: 1,
          withholdingTaxImportedService: 1,
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        new TaxRule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TaxRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          telcoExciseDuty: 1,
          valueAddedTax: 1,
          withholdingVAT: 1,
          withholdingTaxConsultancy: 1,
          withholdingTaxRent: 1,
          cateringLevy: 1,
          serviceCharge: 1,
          withholdingTaxImportedService: 1,
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

    it('should delete a TaxRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTaxRuleToCollectionIfMissing', () => {
      it('should add a TaxRule to an empty array', () => {
        const taxRule: ITaxRule = { id: 123 };
        expectedResult = service.addTaxRuleToCollectionIfMissing([], taxRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxRule);
      });

      it('should not add a TaxRule to an array that contains it', () => {
        const taxRule: ITaxRule = { id: 123 };
        const taxRuleCollection: ITaxRule[] = [
          {
            ...taxRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addTaxRuleToCollectionIfMissing(taxRuleCollection, taxRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TaxRule to an array that doesn't contain it", () => {
        const taxRule: ITaxRule = { id: 123 };
        const taxRuleCollection: ITaxRule[] = [{ id: 456 }];
        expectedResult = service.addTaxRuleToCollectionIfMissing(taxRuleCollection, taxRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxRule);
      });

      it('should add only unique TaxRule to an array', () => {
        const taxRuleArray: ITaxRule[] = [{ id: 123 }, { id: 456 }, { id: 68979 }];
        const taxRuleCollection: ITaxRule[] = [{ id: 123 }];
        expectedResult = service.addTaxRuleToCollectionIfMissing(taxRuleCollection, ...taxRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taxRule: ITaxRule = { id: 123 };
        const taxRule2: ITaxRule = { id: 456 };
        expectedResult = service.addTaxRuleToCollectionIfMissing([], taxRule, taxRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxRule);
        expect(expectedResult).toContain(taxRule2);
      });

      it('should accept null and undefined values', () => {
        const taxRule: ITaxRule = { id: 123 };
        expectedResult = service.addTaxRuleToCollectionIfMissing([], null, taxRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(taxRule);
      });

      it('should return initial array if no TaxRule is added', () => {
        const taxRuleCollection: ITaxRule[] = [{ id: 123 }];
        expectedResult = service.addTaxRuleToCollectionIfMissing(taxRuleCollection, undefined, null);
        expect(expectedResult).toEqual(taxRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
