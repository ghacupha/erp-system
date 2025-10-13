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

import { IAcademicQualification, AcademicQualification } from '../academic-qualification.model';

import { AcademicQualificationService } from './academic-qualification.service';

describe('AcademicQualification Service', () => {
  let service: AcademicQualificationService;
  let httpMock: HttpTestingController;
  let elemDefault: IAcademicQualification;
  let expectedResult: IAcademicQualification | IAcademicQualification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AcademicQualificationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      academicQualificationsCode: 'AAAAAAA',
      academicQualificationType: 'AAAAAAA',
      academicQualificationTypeDetail: 'AAAAAAA',
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

    it('should create a AcademicQualification', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AcademicQualification()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AcademicQualification', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          academicQualificationsCode: 'BBBBBB',
          academicQualificationType: 'BBBBBB',
          academicQualificationTypeDetail: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AcademicQualification', () => {
      const patchObject = Object.assign({}, new AcademicQualification());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AcademicQualification', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          academicQualificationsCode: 'BBBBBB',
          academicQualificationType: 'BBBBBB',
          academicQualificationTypeDetail: 'BBBBBB',
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

    it('should delete a AcademicQualification', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAcademicQualificationToCollectionIfMissing', () => {
      it('should add a AcademicQualification to an empty array', () => {
        const academicQualification: IAcademicQualification = { id: 123 };
        expectedResult = service.addAcademicQualificationToCollectionIfMissing([], academicQualification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(academicQualification);
      });

      it('should not add a AcademicQualification to an array that contains it', () => {
        const academicQualification: IAcademicQualification = { id: 123 };
        const academicQualificationCollection: IAcademicQualification[] = [
          {
            ...academicQualification,
          },
          { id: 456 },
        ];
        expectedResult = service.addAcademicQualificationToCollectionIfMissing(academicQualificationCollection, academicQualification);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AcademicQualification to an array that doesn't contain it", () => {
        const academicQualification: IAcademicQualification = { id: 123 };
        const academicQualificationCollection: IAcademicQualification[] = [{ id: 456 }];
        expectedResult = service.addAcademicQualificationToCollectionIfMissing(academicQualificationCollection, academicQualification);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(academicQualification);
      });

      it('should add only unique AcademicQualification to an array', () => {
        const academicQualificationArray: IAcademicQualification[] = [{ id: 123 }, { id: 456 }, { id: 47882 }];
        const academicQualificationCollection: IAcademicQualification[] = [{ id: 123 }];
        expectedResult = service.addAcademicQualificationToCollectionIfMissing(
          academicQualificationCollection,
          ...academicQualificationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const academicQualification: IAcademicQualification = { id: 123 };
        const academicQualification2: IAcademicQualification = { id: 456 };
        expectedResult = service.addAcademicQualificationToCollectionIfMissing([], academicQualification, academicQualification2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(academicQualification);
        expect(expectedResult).toContain(academicQualification2);
      });

      it('should accept null and undefined values', () => {
        const academicQualification: IAcademicQualification = { id: 123 };
        expectedResult = service.addAcademicQualificationToCollectionIfMissing([], null, academicQualification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(academicQualification);
      });

      it('should return initial array if no AcademicQualification is added', () => {
        const academicQualificationCollection: IAcademicQualification[] = [{ id: 123 }];
        expectedResult = service.addAcademicQualificationToCollectionIfMissing(academicQualificationCollection, undefined, null);
        expect(expectedResult).toEqual(academicQualificationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
