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
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRelatedPartyRelationship, RelatedPartyRelationship } from '../related-party-relationship.model';

import { RelatedPartyRelationshipService } from './related-party-relationship.service';

describe('RelatedPartyRelationship Service', () => {
  let service: RelatedPartyRelationshipService;
  let httpMock: HttpTestingController;
  let elemDefault: IRelatedPartyRelationship;
  let expectedResult: IRelatedPartyRelationship | IRelatedPartyRelationship[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RelatedPartyRelationshipService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      reportingDate: currentDate,
      customerId: 'AAAAAAA',
      relatedPartyId: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RelatedPartyRelationship', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          reportingDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.create(new RelatedPartyRelationship()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RelatedPartyRelationship', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          customerId: 'BBBBBB',
          relatedPartyId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RelatedPartyRelationship', () => {
      const patchObject = Object.assign(
        {
          reportingDate: currentDate.format(DATE_FORMAT),
          relatedPartyId: 'BBBBBB',
        },
        new RelatedPartyRelationship()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RelatedPartyRelationship', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reportingDate: currentDate.format(DATE_FORMAT),
          customerId: 'BBBBBB',
          relatedPartyId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          reportingDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RelatedPartyRelationship', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRelatedPartyRelationshipToCollectionIfMissing', () => {
      it('should add a RelatedPartyRelationship to an empty array', () => {
        const relatedPartyRelationship: IRelatedPartyRelationship = { id: 123 };
        expectedResult = service.addRelatedPartyRelationshipToCollectionIfMissing([], relatedPartyRelationship);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(relatedPartyRelationship);
      });

      it('should not add a RelatedPartyRelationship to an array that contains it', () => {
        const relatedPartyRelationship: IRelatedPartyRelationship = { id: 123 };
        const relatedPartyRelationshipCollection: IRelatedPartyRelationship[] = [
          {
            ...relatedPartyRelationship,
          },
          { id: 456 },
        ];
        expectedResult = service.addRelatedPartyRelationshipToCollectionIfMissing(
          relatedPartyRelationshipCollection,
          relatedPartyRelationship
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RelatedPartyRelationship to an array that doesn't contain it", () => {
        const relatedPartyRelationship: IRelatedPartyRelationship = { id: 123 };
        const relatedPartyRelationshipCollection: IRelatedPartyRelationship[] = [{ id: 456 }];
        expectedResult = service.addRelatedPartyRelationshipToCollectionIfMissing(
          relatedPartyRelationshipCollection,
          relatedPartyRelationship
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(relatedPartyRelationship);
      });

      it('should add only unique RelatedPartyRelationship to an array', () => {
        const relatedPartyRelationshipArray: IRelatedPartyRelationship[] = [{ id: 123 }, { id: 456 }, { id: 5491 }];
        const relatedPartyRelationshipCollection: IRelatedPartyRelationship[] = [{ id: 123 }];
        expectedResult = service.addRelatedPartyRelationshipToCollectionIfMissing(
          relatedPartyRelationshipCollection,
          ...relatedPartyRelationshipArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const relatedPartyRelationship: IRelatedPartyRelationship = { id: 123 };
        const relatedPartyRelationship2: IRelatedPartyRelationship = { id: 456 };
        expectedResult = service.addRelatedPartyRelationshipToCollectionIfMissing([], relatedPartyRelationship, relatedPartyRelationship2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(relatedPartyRelationship);
        expect(expectedResult).toContain(relatedPartyRelationship2);
      });

      it('should accept null and undefined values', () => {
        const relatedPartyRelationship: IRelatedPartyRelationship = { id: 123 };
        expectedResult = service.addRelatedPartyRelationshipToCollectionIfMissing([], null, relatedPartyRelationship, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(relatedPartyRelationship);
      });

      it('should return initial array if no RelatedPartyRelationship is added', () => {
        const relatedPartyRelationshipCollection: IRelatedPartyRelationship[] = [{ id: 123 }];
        expectedResult = service.addRelatedPartyRelationshipToCollectionIfMissing(relatedPartyRelationshipCollection, undefined, null);
        expect(expectedResult).toEqual(relatedPartyRelationshipCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
