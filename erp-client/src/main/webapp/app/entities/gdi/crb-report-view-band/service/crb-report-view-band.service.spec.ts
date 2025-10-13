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

import { ICrbReportViewBand, CrbReportViewBand } from '../crb-report-view-band.model';

import { CrbReportViewBandService } from './crb-report-view-band.service';

describe('CrbReportViewBand Service', () => {
  let service: CrbReportViewBandService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbReportViewBand;
  let expectedResult: ICrbReportViewBand | ICrbReportViewBand[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbReportViewBandService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      reportViewCode: 'AAAAAAA',
      reportViewCategory: 'AAAAAAA',
      reportViewCategoryDescription: 'AAAAAAA',
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

    it('should create a CrbReportViewBand', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbReportViewBand()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbReportViewBand', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportViewCode: 'BBBBBB',
          reportViewCategory: 'BBBBBB',
          reportViewCategoryDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbReportViewBand', () => {
      const patchObject = Object.assign(
        {
          reportViewCategory: 'BBBBBB',
          reportViewCategoryDescription: 'BBBBBB',
        },
        new CrbReportViewBand()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbReportViewBand', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportViewCode: 'BBBBBB',
          reportViewCategory: 'BBBBBB',
          reportViewCategoryDescription: 'BBBBBB',
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

    it('should delete a CrbReportViewBand', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbReportViewBandToCollectionIfMissing', () => {
      it('should add a CrbReportViewBand to an empty array', () => {
        const crbReportViewBand: ICrbReportViewBand = { id: 123 };
        expectedResult = service.addCrbReportViewBandToCollectionIfMissing([], crbReportViewBand);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbReportViewBand);
      });

      it('should not add a CrbReportViewBand to an array that contains it', () => {
        const crbReportViewBand: ICrbReportViewBand = { id: 123 };
        const crbReportViewBandCollection: ICrbReportViewBand[] = [
          {
            ...crbReportViewBand,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbReportViewBandToCollectionIfMissing(crbReportViewBandCollection, crbReportViewBand);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbReportViewBand to an array that doesn't contain it", () => {
        const crbReportViewBand: ICrbReportViewBand = { id: 123 };
        const crbReportViewBandCollection: ICrbReportViewBand[] = [{ id: 456 }];
        expectedResult = service.addCrbReportViewBandToCollectionIfMissing(crbReportViewBandCollection, crbReportViewBand);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbReportViewBand);
      });

      it('should add only unique CrbReportViewBand to an array', () => {
        const crbReportViewBandArray: ICrbReportViewBand[] = [{ id: 123 }, { id: 456 }, { id: 86626 }];
        const crbReportViewBandCollection: ICrbReportViewBand[] = [{ id: 123 }];
        expectedResult = service.addCrbReportViewBandToCollectionIfMissing(crbReportViewBandCollection, ...crbReportViewBandArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbReportViewBand: ICrbReportViewBand = { id: 123 };
        const crbReportViewBand2: ICrbReportViewBand = { id: 456 };
        expectedResult = service.addCrbReportViewBandToCollectionIfMissing([], crbReportViewBand, crbReportViewBand2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbReportViewBand);
        expect(expectedResult).toContain(crbReportViewBand2);
      });

      it('should accept null and undefined values', () => {
        const crbReportViewBand: ICrbReportViewBand = { id: 123 };
        expectedResult = service.addCrbReportViewBandToCollectionIfMissing([], null, crbReportViewBand, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbReportViewBand);
      });

      it('should return initial array if no CrbReportViewBand is added', () => {
        const crbReportViewBandCollection: ICrbReportViewBand[] = [{ id: 123 }];
        expectedResult = service.addCrbReportViewBandToCollectionIfMissing(crbReportViewBandCollection, undefined, null);
        expect(expectedResult).toEqual(crbReportViewBandCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
