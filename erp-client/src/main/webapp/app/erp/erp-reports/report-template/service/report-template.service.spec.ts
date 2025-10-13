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

import { IReportTemplate, ReportTemplate } from '../report-template.model';

import { ReportTemplateService } from './report-template.service';

describe('ReportTemplate Service', () => {
  let service: ReportTemplateService;
  let httpMock: HttpTestingController;
  let elemDefault: IReportTemplate;
  let expectedResult: IReportTemplate | IReportTemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportTemplateService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      catalogueNumber: 'AAAAAAA',
      description: 'AAAAAAA',
      notesContentType: 'image/png',
      notes: 'AAAAAAA',
      reportFileContentType: 'image/png',
      reportFile: 'AAAAAAA',
      compileReportFileContentType: 'image/png',
      compileReportFile: 'AAAAAAA',
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

    it('should create a ReportTemplate', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ReportTemplate()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportTemplate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          description: 'BBBBBB',
          notes: 'BBBBBB',
          reportFile: 'BBBBBB',
          compileReportFile: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportTemplate', () => {
      const patchObject = Object.assign(
        {
          catalogueNumber: 'BBBBBB',
          description: 'BBBBBB',
          reportFile: 'BBBBBB',
          compileReportFile: 'BBBBBB',
        },
        new ReportTemplate()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportTemplate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          description: 'BBBBBB',
          notes: 'BBBBBB',
          reportFile: 'BBBBBB',
          compileReportFile: 'BBBBBB',
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

    it('should delete a ReportTemplate', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReportTemplateToCollectionIfMissing', () => {
      it('should add a ReportTemplate to an empty array', () => {
        const reportTemplate: IReportTemplate = { id: 123 };
        expectedResult = service.addReportTemplateToCollectionIfMissing([], reportTemplate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportTemplate);
      });

      it('should not add a ReportTemplate to an array that contains it', () => {
        const reportTemplate: IReportTemplate = { id: 123 };
        const reportTemplateCollection: IReportTemplate[] = [
          {
            ...reportTemplate,
          },
          { id: 456 },
        ];
        expectedResult = service.addReportTemplateToCollectionIfMissing(reportTemplateCollection, reportTemplate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportTemplate to an array that doesn't contain it", () => {
        const reportTemplate: IReportTemplate = { id: 123 };
        const reportTemplateCollection: IReportTemplate[] = [{ id: 456 }];
        expectedResult = service.addReportTemplateToCollectionIfMissing(reportTemplateCollection, reportTemplate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportTemplate);
      });

      it('should add only unique ReportTemplate to an array', () => {
        const reportTemplateArray: IReportTemplate[] = [{ id: 123 }, { id: 456 }, { id: 32863 }];
        const reportTemplateCollection: IReportTemplate[] = [{ id: 123 }];
        expectedResult = service.addReportTemplateToCollectionIfMissing(reportTemplateCollection, ...reportTemplateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportTemplate: IReportTemplate = { id: 123 };
        const reportTemplate2: IReportTemplate = { id: 456 };
        expectedResult = service.addReportTemplateToCollectionIfMissing([], reportTemplate, reportTemplate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportTemplate);
        expect(expectedResult).toContain(reportTemplate2);
      });

      it('should accept null and undefined values', () => {
        const reportTemplate: IReportTemplate = { id: 123 };
        expectedResult = service.addReportTemplateToCollectionIfMissing([], null, reportTemplate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportTemplate);
      });

      it('should return initial array if no ReportTemplate is added', () => {
        const reportTemplateCollection: IReportTemplate[] = [{ id: 123 }];
        expectedResult = service.addReportTemplateToCollectionIfMissing(reportTemplateCollection, undefined, null);
        expect(expectedResult).toEqual(reportTemplateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
