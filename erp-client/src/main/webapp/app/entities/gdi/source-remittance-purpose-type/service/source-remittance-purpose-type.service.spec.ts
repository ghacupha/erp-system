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

import { SourceOrPurposeOfRemittancFlag } from 'app/entities/enumerations/source-or-purpose-of-remittanc-flag.model';
import { ISourceRemittancePurposeType, SourceRemittancePurposeType } from '../source-remittance-purpose-type.model';

import { SourceRemittancePurposeTypeService } from './source-remittance-purpose-type.service';

describe('SourceRemittancePurposeType Service', () => {
  let service: SourceRemittancePurposeTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISourceRemittancePurposeType;
  let expectedResult: ISourceRemittancePurposeType | ISourceRemittancePurposeType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SourceRemittancePurposeTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      sourceOrPurposeTypeCode: 'AAAAAAA',
      sourceOrPurposeOfRemittanceFlag: SourceOrPurposeOfRemittancFlag.PURPOSE_OF_REMITTANCE,
      sourceOrPurposeOfRemittanceType: 'AAAAAAA',
      remittancePurposeTypeDetails: 'AAAAAAA',
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

    it('should create a SourceRemittancePurposeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SourceRemittancePurposeType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SourceRemittancePurposeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceOrPurposeTypeCode: 'BBBBBB',
          sourceOrPurposeOfRemittanceFlag: 'BBBBBB',
          sourceOrPurposeOfRemittanceType: 'BBBBBB',
          remittancePurposeTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SourceRemittancePurposeType', () => {
      const patchObject = Object.assign(
        {
          sourceOrPurposeOfRemittanceFlag: 'BBBBBB',
          sourceOrPurposeOfRemittanceType: 'BBBBBB',
        },
        new SourceRemittancePurposeType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SourceRemittancePurposeType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          sourceOrPurposeTypeCode: 'BBBBBB',
          sourceOrPurposeOfRemittanceFlag: 'BBBBBB',
          sourceOrPurposeOfRemittanceType: 'BBBBBB',
          remittancePurposeTypeDetails: 'BBBBBB',
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

    it('should delete a SourceRemittancePurposeType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSourceRemittancePurposeTypeToCollectionIfMissing', () => {
      it('should add a SourceRemittancePurposeType to an empty array', () => {
        const sourceRemittancePurposeType: ISourceRemittancePurposeType = { id: 123 };
        expectedResult = service.addSourceRemittancePurposeTypeToCollectionIfMissing([], sourceRemittancePurposeType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sourceRemittancePurposeType);
      });

      it('should not add a SourceRemittancePurposeType to an array that contains it', () => {
        const sourceRemittancePurposeType: ISourceRemittancePurposeType = { id: 123 };
        const sourceRemittancePurposeTypeCollection: ISourceRemittancePurposeType[] = [
          {
            ...sourceRemittancePurposeType,
          },
          { id: 456 },
        ];
        expectedResult = service.addSourceRemittancePurposeTypeToCollectionIfMissing(
          sourceRemittancePurposeTypeCollection,
          sourceRemittancePurposeType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SourceRemittancePurposeType to an array that doesn't contain it", () => {
        const sourceRemittancePurposeType: ISourceRemittancePurposeType = { id: 123 };
        const sourceRemittancePurposeTypeCollection: ISourceRemittancePurposeType[] = [{ id: 456 }];
        expectedResult = service.addSourceRemittancePurposeTypeToCollectionIfMissing(
          sourceRemittancePurposeTypeCollection,
          sourceRemittancePurposeType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sourceRemittancePurposeType);
      });

      it('should add only unique SourceRemittancePurposeType to an array', () => {
        const sourceRemittancePurposeTypeArray: ISourceRemittancePurposeType[] = [{ id: 123 }, { id: 456 }, { id: 69988 }];
        const sourceRemittancePurposeTypeCollection: ISourceRemittancePurposeType[] = [{ id: 123 }];
        expectedResult = service.addSourceRemittancePurposeTypeToCollectionIfMissing(
          sourceRemittancePurposeTypeCollection,
          ...sourceRemittancePurposeTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sourceRemittancePurposeType: ISourceRemittancePurposeType = { id: 123 };
        const sourceRemittancePurposeType2: ISourceRemittancePurposeType = { id: 456 };
        expectedResult = service.addSourceRemittancePurposeTypeToCollectionIfMissing(
          [],
          sourceRemittancePurposeType,
          sourceRemittancePurposeType2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sourceRemittancePurposeType);
        expect(expectedResult).toContain(sourceRemittancePurposeType2);
      });

      it('should accept null and undefined values', () => {
        const sourceRemittancePurposeType: ISourceRemittancePurposeType = { id: 123 };
        expectedResult = service.addSourceRemittancePurposeTypeToCollectionIfMissing([], null, sourceRemittancePurposeType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sourceRemittancePurposeType);
      });

      it('should return initial array if no SourceRemittancePurposeType is added', () => {
        const sourceRemittancePurposeTypeCollection: ISourceRemittancePurposeType[] = [{ id: 123 }];
        expectedResult = service.addSourceRemittancePurposeTypeToCollectionIfMissing(
          sourceRemittancePurposeTypeCollection,
          undefined,
          null
        );
        expect(expectedResult).toEqual(sourceRemittancePurposeTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
