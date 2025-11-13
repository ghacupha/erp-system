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

import { IProfessionalQualification, ProfessionalQualification } from '../professional-qualification.model';

import { ProfessionalQualificationService } from './professional-qualification.service';

describe('ProfessionalQualification Service', () => {
  let service: ProfessionalQualificationService;
  let httpMock: HttpTestingController;
  let elemDefault: IProfessionalQualification;
  let expectedResult: IProfessionalQualification | IProfessionalQualification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProfessionalQualificationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      professionalQualificationsCode: 'AAAAAAA',
      professionalQualificationsType: 'AAAAAAA',
      professionalQualificationsDetails: 'AAAAAAA',
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

    it('should create a ProfessionalQualification', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ProfessionalQualification()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProfessionalQualification', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          professionalQualificationsCode: 'BBBBBB',
          professionalQualificationsType: 'BBBBBB',
          professionalQualificationsDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProfessionalQualification', () => {
      const patchObject = Object.assign(
        {
          professionalQualificationsCode: 'BBBBBB',
          professionalQualificationsType: 'BBBBBB',
          professionalQualificationsDetails: 'BBBBBB',
        },
        new ProfessionalQualification()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProfessionalQualification', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          professionalQualificationsCode: 'BBBBBB',
          professionalQualificationsType: 'BBBBBB',
          professionalQualificationsDetails: 'BBBBBB',
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

    it('should delete a ProfessionalQualification', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProfessionalQualificationToCollectionIfMissing', () => {
      it('should add a ProfessionalQualification to an empty array', () => {
        const professionalQualification: IProfessionalQualification = { id: 123 };
        expectedResult = service.addProfessionalQualificationToCollectionIfMissing([], professionalQualification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(professionalQualification);
      });

      it('should not add a ProfessionalQualification to an array that contains it', () => {
        const professionalQualification: IProfessionalQualification = { id: 123 };
        const professionalQualificationCollection: IProfessionalQualification[] = [
          {
            ...professionalQualification,
          },
          { id: 456 },
        ];
        expectedResult = service.addProfessionalQualificationToCollectionIfMissing(
          professionalQualificationCollection,
          professionalQualification
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProfessionalQualification to an array that doesn't contain it", () => {
        const professionalQualification: IProfessionalQualification = { id: 123 };
        const professionalQualificationCollection: IProfessionalQualification[] = [{ id: 456 }];
        expectedResult = service.addProfessionalQualificationToCollectionIfMissing(
          professionalQualificationCollection,
          professionalQualification
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(professionalQualification);
      });

      it('should add only unique ProfessionalQualification to an array', () => {
        const professionalQualificationArray: IProfessionalQualification[] = [{ id: 123 }, { id: 456 }, { id: 51188 }];
        const professionalQualificationCollection: IProfessionalQualification[] = [{ id: 123 }];
        expectedResult = service.addProfessionalQualificationToCollectionIfMissing(
          professionalQualificationCollection,
          ...professionalQualificationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const professionalQualification: IProfessionalQualification = { id: 123 };
        const professionalQualification2: IProfessionalQualification = { id: 456 };
        expectedResult = service.addProfessionalQualificationToCollectionIfMissing(
          [],
          professionalQualification,
          professionalQualification2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(professionalQualification);
        expect(expectedResult).toContain(professionalQualification2);
      });

      it('should accept null and undefined values', () => {
        const professionalQualification: IProfessionalQualification = { id: 123 };
        expectedResult = service.addProfessionalQualificationToCollectionIfMissing([], null, professionalQualification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(professionalQualification);
      });

      it('should return initial array if no ProfessionalQualification is added', () => {
        const professionalQualificationCollection: IProfessionalQualification[] = [{ id: 123 }];
        expectedResult = service.addProfessionalQualificationToCollectionIfMissing(professionalQualificationCollection, undefined, null);
        expect(expectedResult).toEqual(professionalQualificationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
