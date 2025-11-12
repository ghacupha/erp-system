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

import { ICrbComplaintStatusType, CrbComplaintStatusType } from '../crb-complaint-status-type.model';

import { CrbComplaintStatusTypeService } from './crb-complaint-status-type.service';

describe('CrbComplaintStatusType Service', () => {
  let service: CrbComplaintStatusTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbComplaintStatusType;
  let expectedResult: ICrbComplaintStatusType | ICrbComplaintStatusType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbComplaintStatusTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      complaintStatusTypeCode: 'AAAAAAA',
      complaintStatusType: 'AAAAAAA',
      complaintStatusDetails: 'AAAAAAA',
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

    it('should create a CrbComplaintStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbComplaintStatusType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbComplaintStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          complaintStatusTypeCode: 'BBBBBB',
          complaintStatusType: 'BBBBBB',
          complaintStatusDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbComplaintStatusType', () => {
      const patchObject = Object.assign(
        {
          complaintStatusDetails: 'BBBBBB',
        },
        new CrbComplaintStatusType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbComplaintStatusType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          complaintStatusTypeCode: 'BBBBBB',
          complaintStatusType: 'BBBBBB',
          complaintStatusDetails: 'BBBBBB',
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

    it('should delete a CrbComplaintStatusType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbComplaintStatusTypeToCollectionIfMissing', () => {
      it('should add a CrbComplaintStatusType to an empty array', () => {
        const crbComplaintStatusType: ICrbComplaintStatusType = { id: 123 };
        expectedResult = service.addCrbComplaintStatusTypeToCollectionIfMissing([], crbComplaintStatusType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbComplaintStatusType);
      });

      it('should not add a CrbComplaintStatusType to an array that contains it', () => {
        const crbComplaintStatusType: ICrbComplaintStatusType = { id: 123 };
        const crbComplaintStatusTypeCollection: ICrbComplaintStatusType[] = [
          {
            ...crbComplaintStatusType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbComplaintStatusTypeToCollectionIfMissing(crbComplaintStatusTypeCollection, crbComplaintStatusType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbComplaintStatusType to an array that doesn't contain it", () => {
        const crbComplaintStatusType: ICrbComplaintStatusType = { id: 123 };
        const crbComplaintStatusTypeCollection: ICrbComplaintStatusType[] = [{ id: 456 }];
        expectedResult = service.addCrbComplaintStatusTypeToCollectionIfMissing(crbComplaintStatusTypeCollection, crbComplaintStatusType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbComplaintStatusType);
      });

      it('should add only unique CrbComplaintStatusType to an array', () => {
        const crbComplaintStatusTypeArray: ICrbComplaintStatusType[] = [{ id: 123 }, { id: 456 }, { id: 98719 }];
        const crbComplaintStatusTypeCollection: ICrbComplaintStatusType[] = [{ id: 123 }];
        expectedResult = service.addCrbComplaintStatusTypeToCollectionIfMissing(
          crbComplaintStatusTypeCollection,
          ...crbComplaintStatusTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbComplaintStatusType: ICrbComplaintStatusType = { id: 123 };
        const crbComplaintStatusType2: ICrbComplaintStatusType = { id: 456 };
        expectedResult = service.addCrbComplaintStatusTypeToCollectionIfMissing([], crbComplaintStatusType, crbComplaintStatusType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbComplaintStatusType);
        expect(expectedResult).toContain(crbComplaintStatusType2);
      });

      it('should accept null and undefined values', () => {
        const crbComplaintStatusType: ICrbComplaintStatusType = { id: 123 };
        expectedResult = service.addCrbComplaintStatusTypeToCollectionIfMissing([], null, crbComplaintStatusType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbComplaintStatusType);
      });

      it('should return initial array if no CrbComplaintStatusType is added', () => {
        const crbComplaintStatusTypeCollection: ICrbComplaintStatusType[] = [{ id: 123 }];
        expectedResult = service.addCrbComplaintStatusTypeToCollectionIfMissing(crbComplaintStatusTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbComplaintStatusTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
