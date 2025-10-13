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

import { INatureOfCustomerComplaints, NatureOfCustomerComplaints } from '../nature-of-customer-complaints.model';

import { NatureOfCustomerComplaintsService } from './nature-of-customer-complaints.service';

describe('NatureOfCustomerComplaints Service', () => {
  let service: NatureOfCustomerComplaintsService;
  let httpMock: HttpTestingController;
  let elemDefault: INatureOfCustomerComplaints;
  let expectedResult: INatureOfCustomerComplaints | INatureOfCustomerComplaints[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NatureOfCustomerComplaintsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      natureOfComplaintTypeCode: 'AAAAAAA',
      natureOfComplaintType: 'AAAAAAA',
      natureOfComplaintTypeDetails: 'AAAAAAA',
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

    it('should create a NatureOfCustomerComplaints', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NatureOfCustomerComplaints()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NatureOfCustomerComplaints', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          natureOfComplaintTypeCode: 'BBBBBB',
          natureOfComplaintType: 'BBBBBB',
          natureOfComplaintTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NatureOfCustomerComplaints', () => {
      const patchObject = Object.assign(
        {
          natureOfComplaintTypeCode: 'BBBBBB',
        },
        new NatureOfCustomerComplaints()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NatureOfCustomerComplaints', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          natureOfComplaintTypeCode: 'BBBBBB',
          natureOfComplaintType: 'BBBBBB',
          natureOfComplaintTypeDetails: 'BBBBBB',
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

    it('should delete a NatureOfCustomerComplaints', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNatureOfCustomerComplaintsToCollectionIfMissing', () => {
      it('should add a NatureOfCustomerComplaints to an empty array', () => {
        const natureOfCustomerComplaints: INatureOfCustomerComplaints = { id: 123 };
        expectedResult = service.addNatureOfCustomerComplaintsToCollectionIfMissing([], natureOfCustomerComplaints);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(natureOfCustomerComplaints);
      });

      it('should not add a NatureOfCustomerComplaints to an array that contains it', () => {
        const natureOfCustomerComplaints: INatureOfCustomerComplaints = { id: 123 };
        const natureOfCustomerComplaintsCollection: INatureOfCustomerComplaints[] = [
          {
            ...natureOfCustomerComplaints,
          },
          { id: 456 },
        ];
        expectedResult = service.addNatureOfCustomerComplaintsToCollectionIfMissing(
          natureOfCustomerComplaintsCollection,
          natureOfCustomerComplaints
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NatureOfCustomerComplaints to an array that doesn't contain it", () => {
        const natureOfCustomerComplaints: INatureOfCustomerComplaints = { id: 123 };
        const natureOfCustomerComplaintsCollection: INatureOfCustomerComplaints[] = [{ id: 456 }];
        expectedResult = service.addNatureOfCustomerComplaintsToCollectionIfMissing(
          natureOfCustomerComplaintsCollection,
          natureOfCustomerComplaints
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(natureOfCustomerComplaints);
      });

      it('should add only unique NatureOfCustomerComplaints to an array', () => {
        const natureOfCustomerComplaintsArray: INatureOfCustomerComplaints[] = [{ id: 123 }, { id: 456 }, { id: 75940 }];
        const natureOfCustomerComplaintsCollection: INatureOfCustomerComplaints[] = [{ id: 123 }];
        expectedResult = service.addNatureOfCustomerComplaintsToCollectionIfMissing(
          natureOfCustomerComplaintsCollection,
          ...natureOfCustomerComplaintsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const natureOfCustomerComplaints: INatureOfCustomerComplaints = { id: 123 };
        const natureOfCustomerComplaints2: INatureOfCustomerComplaints = { id: 456 };
        expectedResult = service.addNatureOfCustomerComplaintsToCollectionIfMissing(
          [],
          natureOfCustomerComplaints,
          natureOfCustomerComplaints2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(natureOfCustomerComplaints);
        expect(expectedResult).toContain(natureOfCustomerComplaints2);
      });

      it('should accept null and undefined values', () => {
        const natureOfCustomerComplaints: INatureOfCustomerComplaints = { id: 123 };
        expectedResult = service.addNatureOfCustomerComplaintsToCollectionIfMissing([], null, natureOfCustomerComplaints, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(natureOfCustomerComplaints);
      });

      it('should return initial array if no NatureOfCustomerComplaints is added', () => {
        const natureOfCustomerComplaintsCollection: INatureOfCustomerComplaints[] = [{ id: 123 }];
        expectedResult = service.addNatureOfCustomerComplaintsToCollectionIfMissing(natureOfCustomerComplaintsCollection, undefined, null);
        expect(expectedResult).toEqual(natureOfCustomerComplaintsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
