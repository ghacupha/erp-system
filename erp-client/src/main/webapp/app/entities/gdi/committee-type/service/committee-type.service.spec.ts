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

import { ICommitteeType, CommitteeType } from '../committee-type.model';

import { CommitteeTypeService } from './committee-type.service';

describe('CommitteeType Service', () => {
  let service: CommitteeTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommitteeType;
  let expectedResult: ICommitteeType | ICommitteeType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommitteeTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      committeeTypeCode: 'AAAAAAA',
      committeeType: 'AAAAAAA',
      committeeTypeDetails: 'AAAAAAA',
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

    it('should create a CommitteeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CommitteeType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CommitteeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          committeeTypeCode: 'BBBBBB',
          committeeType: 'BBBBBB',
          committeeTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CommitteeType', () => {
      const patchObject = Object.assign({}, new CommitteeType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CommitteeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          committeeTypeCode: 'BBBBBB',
          committeeType: 'BBBBBB',
          committeeTypeDetails: 'BBBBBB',
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

    it('should delete a CommitteeType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommitteeTypeToCollectionIfMissing', () => {
      it('should add a CommitteeType to an empty array', () => {
        const committeeType: ICommitteeType = { id: 123 };
        expectedResult = service.addCommitteeTypeToCollectionIfMissing([], committeeType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(committeeType);
      });

      it('should not add a CommitteeType to an array that contains it', () => {
        const committeeType: ICommitteeType = { id: 123 };
        const committeeTypeCollection: ICommitteeType[] = [
          {
            ...committeeType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCommitteeTypeToCollectionIfMissing(committeeTypeCollection, committeeType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CommitteeType to an array that doesn't contain it", () => {
        const committeeType: ICommitteeType = { id: 123 };
        const committeeTypeCollection: ICommitteeType[] = [{ id: 456 }];
        expectedResult = service.addCommitteeTypeToCollectionIfMissing(committeeTypeCollection, committeeType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(committeeType);
      });

      it('should add only unique CommitteeType to an array', () => {
        const committeeTypeArray: ICommitteeType[] = [{ id: 123 }, { id: 456 }, { id: 8819 }];
        const committeeTypeCollection: ICommitteeType[] = [{ id: 123 }];
        expectedResult = service.addCommitteeTypeToCollectionIfMissing(committeeTypeCollection, ...committeeTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const committeeType: ICommitteeType = { id: 123 };
        const committeeType2: ICommitteeType = { id: 456 };
        expectedResult = service.addCommitteeTypeToCollectionIfMissing([], committeeType, committeeType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(committeeType);
        expect(expectedResult).toContain(committeeType2);
      });

      it('should accept null and undefined values', () => {
        const committeeType: ICommitteeType = { id: 123 };
        expectedResult = service.addCommitteeTypeToCollectionIfMissing([], null, committeeType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(committeeType);
      });

      it('should return initial array if no CommitteeType is added', () => {
        const committeeTypeCollection: ICommitteeType[] = [{ id: 123 }];
        expectedResult = service.addCommitteeTypeToCollectionIfMissing(committeeTypeCollection, undefined, null);
        expect(expectedResult).toEqual(committeeTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
