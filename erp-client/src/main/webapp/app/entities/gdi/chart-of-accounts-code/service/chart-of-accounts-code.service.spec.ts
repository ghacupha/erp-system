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

import { IChartOfAccountsCode, ChartOfAccountsCode } from '../chart-of-accounts-code.model';

import { ChartOfAccountsCodeService } from './chart-of-accounts-code.service';

describe('ChartOfAccountsCode Service', () => {
  let service: ChartOfAccountsCodeService;
  let httpMock: HttpTestingController;
  let elemDefault: IChartOfAccountsCode;
  let expectedResult: IChartOfAccountsCode | IChartOfAccountsCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChartOfAccountsCodeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      chartOfAccountsCode: 'AAAAAAA',
      chartOfAccountsClass: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a ChartOfAccountsCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ChartOfAccountsCode()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChartOfAccountsCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          chartOfAccountsCode: 'BBBBBB',
          chartOfAccountsClass: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChartOfAccountsCode', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new ChartOfAccountsCode()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChartOfAccountsCode', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          chartOfAccountsCode: 'BBBBBB',
          chartOfAccountsClass: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a ChartOfAccountsCode', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addChartOfAccountsCodeToCollectionIfMissing', () => {
      it('should add a ChartOfAccountsCode to an empty array', () => {
        const chartOfAccountsCode: IChartOfAccountsCode = { id: 123 };
        expectedResult = service.addChartOfAccountsCodeToCollectionIfMissing([], chartOfAccountsCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chartOfAccountsCode);
      });

      it('should not add a ChartOfAccountsCode to an array that contains it', () => {
        const chartOfAccountsCode: IChartOfAccountsCode = { id: 123 };
        const chartOfAccountsCodeCollection: IChartOfAccountsCode[] = [
          {
            ...chartOfAccountsCode,
          },
          { id: 456 },
        ];
        expectedResult = service.addChartOfAccountsCodeToCollectionIfMissing(chartOfAccountsCodeCollection, chartOfAccountsCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChartOfAccountsCode to an array that doesn't contain it", () => {
        const chartOfAccountsCode: IChartOfAccountsCode = { id: 123 };
        const chartOfAccountsCodeCollection: IChartOfAccountsCode[] = [{ id: 456 }];
        expectedResult = service.addChartOfAccountsCodeToCollectionIfMissing(chartOfAccountsCodeCollection, chartOfAccountsCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chartOfAccountsCode);
      });

      it('should add only unique ChartOfAccountsCode to an array', () => {
        const chartOfAccountsCodeArray: IChartOfAccountsCode[] = [{ id: 123 }, { id: 456 }, { id: 7894 }];
        const chartOfAccountsCodeCollection: IChartOfAccountsCode[] = [{ id: 123 }];
        expectedResult = service.addChartOfAccountsCodeToCollectionIfMissing(chartOfAccountsCodeCollection, ...chartOfAccountsCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const chartOfAccountsCode: IChartOfAccountsCode = { id: 123 };
        const chartOfAccountsCode2: IChartOfAccountsCode = { id: 456 };
        expectedResult = service.addChartOfAccountsCodeToCollectionIfMissing([], chartOfAccountsCode, chartOfAccountsCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chartOfAccountsCode);
        expect(expectedResult).toContain(chartOfAccountsCode2);
      });

      it('should accept null and undefined values', () => {
        const chartOfAccountsCode: IChartOfAccountsCode = { id: 123 };
        expectedResult = service.addChartOfAccountsCodeToCollectionIfMissing([], null, chartOfAccountsCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chartOfAccountsCode);
      });

      it('should return initial array if no ChartOfAccountsCode is added', () => {
        const chartOfAccountsCodeCollection: IChartOfAccountsCode[] = [{ id: 123 }];
        expectedResult = service.addChartOfAccountsCodeToCollectionIfMissing(chartOfAccountsCodeCollection, undefined, null);
        expect(expectedResult).toEqual(chartOfAccountsCodeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
