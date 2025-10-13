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

import { IReportDesign, ReportDesign } from '../report-design.model';

import { ReportDesignService } from './report-design.service';

describe('ReportDesign Service', () => {
  let service: ReportDesignService;
  let httpMock: HttpTestingController;
  let elemDefault: IReportDesign;
  let expectedResult: IReportDesign | IReportDesign[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportDesignService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      catalogueNumber: 'AAAAAAA',
      designation: 'AAAAAAA',
      description: 'AAAAAAA',
      notesContentType: 'image/png',
      notes: 'AAAAAAA',
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
      reportFileChecksum: 'AAAAAAA',
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

    it('should create a ReportDesign', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ReportDesign()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportDesign', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
          notes: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportFileChecksum: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportDesign', () => {
      const patchObject = Object.assign(
        {
          designation: 'BBBBBB',
          description: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportFileChecksum: 'BBBBBB',
        },
        new ReportDesign()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportDesign', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          designation: 'BBBBBB',
          description: 'BBBBBB',
          notes: 'BBBBBB',
          reportFile: 'BBBBBB',
          reportFileChecksum: 'BBBBBB',
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

    it('should delete a ReportDesign', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReportDesignToCollectionIfMissing', () => {
      it('should add a ReportDesign to an empty array', () => {
        const reportDesign: IReportDesign = { id: 123 };
        expectedResult = service.addReportDesignToCollectionIfMissing([], reportDesign);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportDesign);
      });

      it('should not add a ReportDesign to an array that contains it', () => {
        const reportDesign: IReportDesign = { id: 123 };
        const reportDesignCollection: IReportDesign[] = [
          {
            ...reportDesign,
          },
          { id: 456 },
        ];
        expectedResult = service.addReportDesignToCollectionIfMissing(reportDesignCollection, reportDesign);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportDesign to an array that doesn't contain it", () => {
        const reportDesign: IReportDesign = { id: 123 };
        const reportDesignCollection: IReportDesign[] = [{ id: 456 }];
        expectedResult = service.addReportDesignToCollectionIfMissing(reportDesignCollection, reportDesign);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportDesign);
      });

      it('should add only unique ReportDesign to an array', () => {
        const reportDesignArray: IReportDesign[] = [{ id: 123 }, { id: 456 }, { id: 10823 }];
        const reportDesignCollection: IReportDesign[] = [{ id: 123 }];
        expectedResult = service.addReportDesignToCollectionIfMissing(reportDesignCollection, ...reportDesignArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportDesign: IReportDesign = { id: 123 };
        const reportDesign2: IReportDesign = { id: 456 };
        expectedResult = service.addReportDesignToCollectionIfMissing([], reportDesign, reportDesign2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportDesign);
        expect(expectedResult).toContain(reportDesign2);
      });

      it('should accept null and undefined values', () => {
        const reportDesign: IReportDesign = { id: 123 };
        expectedResult = service.addReportDesignToCollectionIfMissing([], null, reportDesign, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportDesign);
      });

      it('should return initial array if no ReportDesign is added', () => {
        const reportDesignCollection: IReportDesign[] = [{ id: 123 }];
        expectedResult = service.addReportDesignToCollectionIfMissing(reportDesignCollection, undefined, null);
        expect(expectedResult).toEqual(reportDesignCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
