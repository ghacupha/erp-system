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

import { ICrbAgingBands, CrbAgingBands } from '../crb-aging-bands.model';

import { CrbAgingBandsService } from './crb-aging-bands.service';

describe('CrbAgingBands Service', () => {
  let service: CrbAgingBandsService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbAgingBands;
  let expectedResult: ICrbAgingBands | ICrbAgingBands[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbAgingBandsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      agingBandCategoryCode: 'AAAAAAA',
      agingBandCategory: 'AAAAAAA',
      agingBandCategoryDetails: 'AAAAAAA',
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

    it('should create a CrbAgingBands', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbAgingBands()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbAgingBands', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          agingBandCategoryCode: 'BBBBBB',
          agingBandCategory: 'BBBBBB',
          agingBandCategoryDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbAgingBands', () => {
      const patchObject = Object.assign(
        {
          agingBandCategory: 'BBBBBB',
          agingBandCategoryDetails: 'BBBBBB',
        },
        new CrbAgingBands()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbAgingBands', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          agingBandCategoryCode: 'BBBBBB',
          agingBandCategory: 'BBBBBB',
          agingBandCategoryDetails: 'BBBBBB',
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

    it('should delete a CrbAgingBands', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbAgingBandsToCollectionIfMissing', () => {
      it('should add a CrbAgingBands to an empty array', () => {
        const crbAgingBands: ICrbAgingBands = { id: 123 };
        expectedResult = service.addCrbAgingBandsToCollectionIfMissing([], crbAgingBands);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAgingBands);
      });

      it('should not add a CrbAgingBands to an array that contains it', () => {
        const crbAgingBands: ICrbAgingBands = { id: 123 };
        const crbAgingBandsCollection: ICrbAgingBands[] = [
          {
            ...crbAgingBands,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbAgingBandsToCollectionIfMissing(crbAgingBandsCollection, crbAgingBands);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbAgingBands to an array that doesn't contain it", () => {
        const crbAgingBands: ICrbAgingBands = { id: 123 };
        const crbAgingBandsCollection: ICrbAgingBands[] = [{ id: 456 }];
        expectedResult = service.addCrbAgingBandsToCollectionIfMissing(crbAgingBandsCollection, crbAgingBands);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAgingBands);
      });

      it('should add only unique CrbAgingBands to an array', () => {
        const crbAgingBandsArray: ICrbAgingBands[] = [{ id: 123 }, { id: 456 }, { id: 30194 }];
        const crbAgingBandsCollection: ICrbAgingBands[] = [{ id: 123 }];
        expectedResult = service.addCrbAgingBandsToCollectionIfMissing(crbAgingBandsCollection, ...crbAgingBandsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbAgingBands: ICrbAgingBands = { id: 123 };
        const crbAgingBands2: ICrbAgingBands = { id: 456 };
        expectedResult = service.addCrbAgingBandsToCollectionIfMissing([], crbAgingBands, crbAgingBands2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAgingBands);
        expect(expectedResult).toContain(crbAgingBands2);
      });

      it('should accept null and undefined values', () => {
        const crbAgingBands: ICrbAgingBands = { id: 123 };
        expectedResult = service.addCrbAgingBandsToCollectionIfMissing([], null, crbAgingBands, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAgingBands);
      });

      it('should return initial array if no CrbAgingBands is added', () => {
        const crbAgingBandsCollection: ICrbAgingBands[] = [{ id: 123 }];
        expectedResult = service.addCrbAgingBandsToCollectionIfMissing(crbAgingBandsCollection, undefined, null);
        expect(expectedResult).toEqual(crbAgingBandsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
