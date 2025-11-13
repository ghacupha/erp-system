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

import { IInstitutionContactDetails, InstitutionContactDetails } from '../institution-contact-details.model';

import { InstitutionContactDetailsService } from './institution-contact-details.service';

describe('InstitutionContactDetails Service', () => {
  let service: InstitutionContactDetailsService;
  let httpMock: HttpTestingController;
  let elemDefault: IInstitutionContactDetails;
  let expectedResult: IInstitutionContactDetails | IInstitutionContactDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InstitutionContactDetailsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      entityId: 'AAAAAAA',
      entityName: 'AAAAAAA',
      contactType: 'AAAAAAA',
      contactLevel: 'AAAAAAA',
      contactValue: 'AAAAAAA',
      contactName: 'AAAAAAA',
      contactDesignation: 'AAAAAAA',
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

    it('should create a InstitutionContactDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InstitutionContactDetails()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InstitutionContactDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entityId: 'BBBBBB',
          entityName: 'BBBBBB',
          contactType: 'BBBBBB',
          contactLevel: 'BBBBBB',
          contactValue: 'BBBBBB',
          contactName: 'BBBBBB',
          contactDesignation: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InstitutionContactDetails', () => {
      const patchObject = Object.assign(
        {
          entityId: 'BBBBBB',
          entityName: 'BBBBBB',
          contactType: 'BBBBBB',
          contactName: 'BBBBBB',
          contactDesignation: 'BBBBBB',
        },
        new InstitutionContactDetails()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InstitutionContactDetails', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          entityId: 'BBBBBB',
          entityName: 'BBBBBB',
          contactType: 'BBBBBB',
          contactLevel: 'BBBBBB',
          contactValue: 'BBBBBB',
          contactName: 'BBBBBB',
          contactDesignation: 'BBBBBB',
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

    it('should delete a InstitutionContactDetails', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInstitutionContactDetailsToCollectionIfMissing', () => {
      it('should add a InstitutionContactDetails to an empty array', () => {
        const institutionContactDetails: IInstitutionContactDetails = { id: 123 };
        expectedResult = service.addInstitutionContactDetailsToCollectionIfMissing([], institutionContactDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institutionContactDetails);
      });

      it('should not add a InstitutionContactDetails to an array that contains it', () => {
        const institutionContactDetails: IInstitutionContactDetails = { id: 123 };
        const institutionContactDetailsCollection: IInstitutionContactDetails[] = [
          {
            ...institutionContactDetails,
          },
          { id: 456 },
        ];
        expectedResult = service.addInstitutionContactDetailsToCollectionIfMissing(
          institutionContactDetailsCollection,
          institutionContactDetails
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InstitutionContactDetails to an array that doesn't contain it", () => {
        const institutionContactDetails: IInstitutionContactDetails = { id: 123 };
        const institutionContactDetailsCollection: IInstitutionContactDetails[] = [{ id: 456 }];
        expectedResult = service.addInstitutionContactDetailsToCollectionIfMissing(
          institutionContactDetailsCollection,
          institutionContactDetails
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institutionContactDetails);
      });

      it('should add only unique InstitutionContactDetails to an array', () => {
        const institutionContactDetailsArray: IInstitutionContactDetails[] = [{ id: 123 }, { id: 456 }, { id: 90823 }];
        const institutionContactDetailsCollection: IInstitutionContactDetails[] = [{ id: 123 }];
        expectedResult = service.addInstitutionContactDetailsToCollectionIfMissing(
          institutionContactDetailsCollection,
          ...institutionContactDetailsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const institutionContactDetails: IInstitutionContactDetails = { id: 123 };
        const institutionContactDetails2: IInstitutionContactDetails = { id: 456 };
        expectedResult = service.addInstitutionContactDetailsToCollectionIfMissing(
          [],
          institutionContactDetails,
          institutionContactDetails2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institutionContactDetails);
        expect(expectedResult).toContain(institutionContactDetails2);
      });

      it('should accept null and undefined values', () => {
        const institutionContactDetails: IInstitutionContactDetails = { id: 123 };
        expectedResult = service.addInstitutionContactDetailsToCollectionIfMissing([], null, institutionContactDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institutionContactDetails);
      });

      it('should return initial array if no InstitutionContactDetails is added', () => {
        const institutionContactDetailsCollection: IInstitutionContactDetails[] = [{ id: 123 }];
        expectedResult = service.addInstitutionContactDetailsToCollectionIfMissing(institutionContactDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(institutionContactDetailsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
