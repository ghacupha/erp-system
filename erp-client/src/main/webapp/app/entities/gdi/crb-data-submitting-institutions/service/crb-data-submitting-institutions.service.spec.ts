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

import { ICrbDataSubmittingInstitutions, CrbDataSubmittingInstitutions } from '../crb-data-submitting-institutions.model';

import { CrbDataSubmittingInstitutionsService } from './crb-data-submitting-institutions.service';

describe('CrbDataSubmittingInstitutions Service', () => {
  let service: CrbDataSubmittingInstitutionsService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbDataSubmittingInstitutions;
  let expectedResult: ICrbDataSubmittingInstitutions | ICrbDataSubmittingInstitutions[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbDataSubmittingInstitutionsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      institutionCode: 'AAAAAAA',
      institutionName: 'AAAAAAA',
      institutionCategory: 'AAAAAAA',
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

    it('should create a CrbDataSubmittingInstitutions', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbDataSubmittingInstitutions()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbDataSubmittingInstitutions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          institutionCode: 'BBBBBB',
          institutionName: 'BBBBBB',
          institutionCategory: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbDataSubmittingInstitutions', () => {
      const patchObject = Object.assign(
        {
          institutionCategory: 'BBBBBB',
        },
        new CrbDataSubmittingInstitutions()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbDataSubmittingInstitutions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          institutionCode: 'BBBBBB',
          institutionName: 'BBBBBB',
          institutionCategory: 'BBBBBB',
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

    it('should delete a CrbDataSubmittingInstitutions', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbDataSubmittingInstitutionsToCollectionIfMissing', () => {
      it('should add a CrbDataSubmittingInstitutions to an empty array', () => {
        const crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions = { id: 123 };
        expectedResult = service.addCrbDataSubmittingInstitutionsToCollectionIfMissing([], crbDataSubmittingInstitutions);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbDataSubmittingInstitutions);
      });

      it('should not add a CrbDataSubmittingInstitutions to an array that contains it', () => {
        const crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions = { id: 123 };
        const crbDataSubmittingInstitutionsCollection: ICrbDataSubmittingInstitutions[] = [
          {
            ...crbDataSubmittingInstitutions,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbDataSubmittingInstitutionsToCollectionIfMissing(
          crbDataSubmittingInstitutionsCollection,
          crbDataSubmittingInstitutions
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbDataSubmittingInstitutions to an array that doesn't contain it", () => {
        const crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions = { id: 123 };
        const crbDataSubmittingInstitutionsCollection: ICrbDataSubmittingInstitutions[] = [{ id: 456 }];
        expectedResult = service.addCrbDataSubmittingInstitutionsToCollectionIfMissing(
          crbDataSubmittingInstitutionsCollection,
          crbDataSubmittingInstitutions
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbDataSubmittingInstitutions);
      });

      it('should add only unique CrbDataSubmittingInstitutions to an array', () => {
        const crbDataSubmittingInstitutionsArray: ICrbDataSubmittingInstitutions[] = [{ id: 123 }, { id: 456 }, { id: 97673 }];
        const crbDataSubmittingInstitutionsCollection: ICrbDataSubmittingInstitutions[] = [{ id: 123 }];
        expectedResult = service.addCrbDataSubmittingInstitutionsToCollectionIfMissing(
          crbDataSubmittingInstitutionsCollection,
          ...crbDataSubmittingInstitutionsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions = { id: 123 };
        const crbDataSubmittingInstitutions2: ICrbDataSubmittingInstitutions = { id: 456 };
        expectedResult = service.addCrbDataSubmittingInstitutionsToCollectionIfMissing(
          [],
          crbDataSubmittingInstitutions,
          crbDataSubmittingInstitutions2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbDataSubmittingInstitutions);
        expect(expectedResult).toContain(crbDataSubmittingInstitutions2);
      });

      it('should accept null and undefined values', () => {
        const crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions = { id: 123 };
        expectedResult = service.addCrbDataSubmittingInstitutionsToCollectionIfMissing([], null, crbDataSubmittingInstitutions, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbDataSubmittingInstitutions);
      });

      it('should return initial array if no CrbDataSubmittingInstitutions is added', () => {
        const crbDataSubmittingInstitutionsCollection: ICrbDataSubmittingInstitutions[] = [{ id: 123 }];
        expectedResult = service.addCrbDataSubmittingInstitutionsToCollectionIfMissing(
          crbDataSubmittingInstitutionsCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(crbDataSubmittingInstitutionsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
