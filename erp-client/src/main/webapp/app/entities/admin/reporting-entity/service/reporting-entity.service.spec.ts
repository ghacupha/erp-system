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

import { IReportingEntity, ReportingEntity } from '../reporting-entity.model';

import { ReportingEntityService } from './reporting-entity.service';

describe('ReportingEntity Service', () => {
  let service: ReportingEntityService;
  let httpMock: HttpTestingController;
  let elemDefault: IReportingEntity;
  let expectedResult: IReportingEntity | IReportingEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReportingEntityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      entityName: 'AAAAAAA',
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

    it('should create a ReportingEntity', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ReportingEntity()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportingEntity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entityName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportingEntity', () => {
      const patchObject = Object.assign({}, new ReportingEntity());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportingEntity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entityName: 'BBBBBB',
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

    it('should delete a ReportingEntity', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReportingEntityToCollectionIfMissing', () => {
      it('should add a ReportingEntity to an empty array', () => {
        const reportingEntity: IReportingEntity = { id: 123 };
        expectedResult = service.addReportingEntityToCollectionIfMissing([], reportingEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingEntity);
      });

      it('should not add a ReportingEntity to an array that contains it', () => {
        const reportingEntity: IReportingEntity = { id: 123 };
        const reportingEntityCollection: IReportingEntity[] = [
          {
            ...reportingEntity,
          },
          { id: 456 },
        ];
        expectedResult = service.addReportingEntityToCollectionIfMissing(reportingEntityCollection, reportingEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportingEntity to an array that doesn't contain it", () => {
        const reportingEntity: IReportingEntity = { id: 123 };
        const reportingEntityCollection: IReportingEntity[] = [{ id: 456 }];
        expectedResult = service.addReportingEntityToCollectionIfMissing(reportingEntityCollection, reportingEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingEntity);
      });

      it('should add only unique ReportingEntity to an array', () => {
        const reportingEntityArray: IReportingEntity[] = [{ id: 123 }, { id: 456 }, { id: 93423 }];
        const reportingEntityCollection: IReportingEntity[] = [{ id: 123 }];
        expectedResult = service.addReportingEntityToCollectionIfMissing(reportingEntityCollection, ...reportingEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportingEntity: IReportingEntity = { id: 123 };
        const reportingEntity2: IReportingEntity = { id: 456 };
        expectedResult = service.addReportingEntityToCollectionIfMissing([], reportingEntity, reportingEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportingEntity);
        expect(expectedResult).toContain(reportingEntity2);
      });

      it('should accept null and undefined values', () => {
        const reportingEntity: IReportingEntity = { id: 123 };
        expectedResult = service.addReportingEntityToCollectionIfMissing([], null, reportingEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportingEntity);
      });

      it('should return initial array if no ReportingEntity is added', () => {
        const reportingEntityCollection: IReportingEntity[] = [{ id: 123 }];
        expectedResult = service.addReportingEntityToCollectionIfMissing(reportingEntityCollection, undefined, null);
        expect(expectedResult).toEqual(reportingEntityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
