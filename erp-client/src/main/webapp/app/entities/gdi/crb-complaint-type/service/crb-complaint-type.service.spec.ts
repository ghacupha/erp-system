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

import { ICrbComplaintType, CrbComplaintType } from '../crb-complaint-type.model';

import { CrbComplaintTypeService } from './crb-complaint-type.service';

describe('CrbComplaintType Service', () => {
  let service: CrbComplaintTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbComplaintType;
  let expectedResult: ICrbComplaintType | ICrbComplaintType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbComplaintTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      complaintTypeCode: 'AAAAAAA',
      complaintType: 'AAAAAAA',
      complaintTypeDetails: 'AAAAAAA',
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

    it('should create a CrbComplaintType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbComplaintType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbComplaintType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          complaintTypeCode: 'BBBBBB',
          complaintType: 'BBBBBB',
          complaintTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbComplaintType', () => {
      const patchObject = Object.assign(
        {
          complaintTypeDetails: 'BBBBBB',
        },
        new CrbComplaintType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbComplaintType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          complaintTypeCode: 'BBBBBB',
          complaintType: 'BBBBBB',
          complaintTypeDetails: 'BBBBBB',
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

    it('should delete a CrbComplaintType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbComplaintTypeToCollectionIfMissing', () => {
      it('should add a CrbComplaintType to an empty array', () => {
        const crbComplaintType: ICrbComplaintType = { id: 123 };
        expectedResult = service.addCrbComplaintTypeToCollectionIfMissing([], crbComplaintType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbComplaintType);
      });

      it('should not add a CrbComplaintType to an array that contains it', () => {
        const crbComplaintType: ICrbComplaintType = { id: 123 };
        const crbComplaintTypeCollection: ICrbComplaintType[] = [
          {
            ...crbComplaintType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbComplaintTypeToCollectionIfMissing(crbComplaintTypeCollection, crbComplaintType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbComplaintType to an array that doesn't contain it", () => {
        const crbComplaintType: ICrbComplaintType = { id: 123 };
        const crbComplaintTypeCollection: ICrbComplaintType[] = [{ id: 456 }];
        expectedResult = service.addCrbComplaintTypeToCollectionIfMissing(crbComplaintTypeCollection, crbComplaintType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbComplaintType);
      });

      it('should add only unique CrbComplaintType to an array', () => {
        const crbComplaintTypeArray: ICrbComplaintType[] = [{ id: 123 }, { id: 456 }, { id: 54204 }];
        const crbComplaintTypeCollection: ICrbComplaintType[] = [{ id: 123 }];
        expectedResult = service.addCrbComplaintTypeToCollectionIfMissing(crbComplaintTypeCollection, ...crbComplaintTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbComplaintType: ICrbComplaintType = { id: 123 };
        const crbComplaintType2: ICrbComplaintType = { id: 456 };
        expectedResult = service.addCrbComplaintTypeToCollectionIfMissing([], crbComplaintType, crbComplaintType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbComplaintType);
        expect(expectedResult).toContain(crbComplaintType2);
      });

      it('should accept null and undefined values', () => {
        const crbComplaintType: ICrbComplaintType = { id: 123 };
        expectedResult = service.addCrbComplaintTypeToCollectionIfMissing([], null, crbComplaintType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbComplaintType);
      });

      it('should return initial array if no CrbComplaintType is added', () => {
        const crbComplaintTypeCollection: ICrbComplaintType[] = [{ id: 123 }];
        expectedResult = service.addCrbComplaintTypeToCollectionIfMissing(crbComplaintTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbComplaintTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
