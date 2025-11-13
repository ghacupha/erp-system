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

import { IReportContentType, ReportContentType } from '../report-content-type.model';

import { ReportContentTypeService } from './report-content-type.service';

describe('ReportContentType Service', () => {
  let service: ReportContentTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IReportContentType;
  let expectedResult: IReportContentType | IReportContentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportContentTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      reportTypeName: 'AAAAAAA',
      reportFileExtension: 'AAAAAAA',
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

    it('should create a ReportContentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ReportContentType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportContentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportTypeName: 'BBBBBB',
          reportFileExtension: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportContentType', () => {
      const patchObject = Object.assign({}, new ReportContentType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportContentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportTypeName: 'BBBBBB',
          reportFileExtension: 'BBBBBB',
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

    it('should delete a ReportContentType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReportContentTypeToCollectionIfMissing', () => {
      it('should add a ReportContentType to an empty array', () => {
        const reportContentType: IReportContentType = { id: 123 };
        expectedResult = service.addReportContentTypeToCollectionIfMissing([], reportContentType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportContentType);
      });

      it('should not add a ReportContentType to an array that contains it', () => {
        const reportContentType: IReportContentType = { id: 123 };
        const reportContentTypeCollection: IReportContentType[] = [
          {
            ...reportContentType,
          },
          { id: 456 },
        ];
        expectedResult = service.addReportContentTypeToCollectionIfMissing(reportContentTypeCollection, reportContentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportContentType to an array that doesn't contain it", () => {
        const reportContentType: IReportContentType = { id: 123 };
        const reportContentTypeCollection: IReportContentType[] = [{ id: 456 }];
        expectedResult = service.addReportContentTypeToCollectionIfMissing(reportContentTypeCollection, reportContentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportContentType);
      });

      it('should add only unique ReportContentType to an array', () => {
        const reportContentTypeArray: IReportContentType[] = [{ id: 123 }, { id: 456 }, { id: 36707 }];
        const reportContentTypeCollection: IReportContentType[] = [{ id: 123 }];
        expectedResult = service.addReportContentTypeToCollectionIfMissing(reportContentTypeCollection, ...reportContentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportContentType: IReportContentType = { id: 123 };
        const reportContentType2: IReportContentType = { id: 456 };
        expectedResult = service.addReportContentTypeToCollectionIfMissing([], reportContentType, reportContentType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportContentType);
        expect(expectedResult).toContain(reportContentType2);
      });

      it('should accept null and undefined values', () => {
        const reportContentType: IReportContentType = { id: 123 };
        expectedResult = service.addReportContentTypeToCollectionIfMissing([], null, reportContentType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportContentType);
      });

      it('should return initial array if no ReportContentType is added', () => {
        const reportContentTypeCollection: IReportContentType[] = [{ id: 123 }];
        expectedResult = service.addReportContentTypeToCollectionIfMissing(reportContentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(reportContentTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
