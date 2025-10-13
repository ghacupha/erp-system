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

import { ICrbAmountCategoryBand, CrbAmountCategoryBand } from '../crb-amount-category-band.model';

import { CrbAmountCategoryBandService } from './crb-amount-category-band.service';

describe('CrbAmountCategoryBand Service', () => {
  let service: CrbAmountCategoryBandService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbAmountCategoryBand;
  let expectedResult: ICrbAmountCategoryBand | ICrbAmountCategoryBand[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbAmountCategoryBandService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      amountCategoryBandCode: 'AAAAAAA',
      amountCategoryBand: 'AAAAAAA',
      amountCategoryBandDetails: 'AAAAAAA',
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

    it('should create a CrbAmountCategoryBand', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbAmountCategoryBand()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbAmountCategoryBand', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amountCategoryBandCode: 'BBBBBB',
          amountCategoryBand: 'BBBBBB',
          amountCategoryBandDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbAmountCategoryBand', () => {
      const patchObject = Object.assign(
        {
          amountCategoryBand: 'BBBBBB',
          amountCategoryBandDetails: 'BBBBBB',
        },
        new CrbAmountCategoryBand()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbAmountCategoryBand', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amountCategoryBandCode: 'BBBBBB',
          amountCategoryBand: 'BBBBBB',
          amountCategoryBandDetails: 'BBBBBB',
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

    it('should delete a CrbAmountCategoryBand', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbAmountCategoryBandToCollectionIfMissing', () => {
      it('should add a CrbAmountCategoryBand to an empty array', () => {
        const crbAmountCategoryBand: ICrbAmountCategoryBand = { id: 123 };
        expectedResult = service.addCrbAmountCategoryBandToCollectionIfMissing([], crbAmountCategoryBand);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAmountCategoryBand);
      });

      it('should not add a CrbAmountCategoryBand to an array that contains it', () => {
        const crbAmountCategoryBand: ICrbAmountCategoryBand = { id: 123 };
        const crbAmountCategoryBandCollection: ICrbAmountCategoryBand[] = [
          {
            ...crbAmountCategoryBand,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbAmountCategoryBandToCollectionIfMissing(crbAmountCategoryBandCollection, crbAmountCategoryBand);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbAmountCategoryBand to an array that doesn't contain it", () => {
        const crbAmountCategoryBand: ICrbAmountCategoryBand = { id: 123 };
        const crbAmountCategoryBandCollection: ICrbAmountCategoryBand[] = [{ id: 456 }];
        expectedResult = service.addCrbAmountCategoryBandToCollectionIfMissing(crbAmountCategoryBandCollection, crbAmountCategoryBand);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAmountCategoryBand);
      });

      it('should add only unique CrbAmountCategoryBand to an array', () => {
        const crbAmountCategoryBandArray: ICrbAmountCategoryBand[] = [{ id: 123 }, { id: 456 }, { id: 5716 }];
        const crbAmountCategoryBandCollection: ICrbAmountCategoryBand[] = [{ id: 123 }];
        expectedResult = service.addCrbAmountCategoryBandToCollectionIfMissing(
          crbAmountCategoryBandCollection,
          ...crbAmountCategoryBandArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbAmountCategoryBand: ICrbAmountCategoryBand = { id: 123 };
        const crbAmountCategoryBand2: ICrbAmountCategoryBand = { id: 456 };
        expectedResult = service.addCrbAmountCategoryBandToCollectionIfMissing([], crbAmountCategoryBand, crbAmountCategoryBand2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAmountCategoryBand);
        expect(expectedResult).toContain(crbAmountCategoryBand2);
      });

      it('should accept null and undefined values', () => {
        const crbAmountCategoryBand: ICrbAmountCategoryBand = { id: 123 };
        expectedResult = service.addCrbAmountCategoryBandToCollectionIfMissing([], null, crbAmountCategoryBand, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAmountCategoryBand);
      });

      it('should return initial array if no CrbAmountCategoryBand is added', () => {
        const crbAmountCategoryBandCollection: ICrbAmountCategoryBand[] = [{ id: 123 }];
        expectedResult = service.addCrbAmountCategoryBandToCollectionIfMissing(crbAmountCategoryBandCollection, undefined, null);
        expect(expectedResult).toEqual(crbAmountCategoryBandCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
