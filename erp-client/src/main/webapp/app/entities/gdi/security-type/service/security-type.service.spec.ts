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

import { ISecurityType, SecurityType } from '../security-type.model';

import { SecurityTypeService } from './security-type.service';

describe('SecurityType Service', () => {
  let service: SecurityTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISecurityType;
  let expectedResult: ISecurityType | ISecurityType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      securityTypeCode: 'AAAAAAA',
      securityType: 'AAAAAAA',
      securityTypeDetails: 'AAAAAAA',
      securityTypeDescription: 'AAAAAAA',
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

    it('should create a SecurityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SecurityType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          securityTypeCode: 'BBBBBB',
          securityType: 'BBBBBB',
          securityTypeDetails: 'BBBBBB',
          securityTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SecurityType', () => {
      const patchObject = Object.assign(
        {
          securityType: 'BBBBBB',
          securityTypeDetails: 'BBBBBB',
        },
        new SecurityType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          securityTypeCode: 'BBBBBB',
          securityType: 'BBBBBB',
          securityTypeDetails: 'BBBBBB',
          securityTypeDescription: 'BBBBBB',
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

    it('should delete a SecurityType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSecurityTypeToCollectionIfMissing', () => {
      it('should add a SecurityType to an empty array', () => {
        const securityType: ISecurityType = { id: 123 };
        expectedResult = service.addSecurityTypeToCollectionIfMissing([], securityType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityType);
      });

      it('should not add a SecurityType to an array that contains it', () => {
        const securityType: ISecurityType = { id: 123 };
        const securityTypeCollection: ISecurityType[] = [
          {
            ...securityType,
          },
          { id: 456 },
        ];
        expectedResult = service.addSecurityTypeToCollectionIfMissing(securityTypeCollection, securityType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityType to an array that doesn't contain it", () => {
        const securityType: ISecurityType = { id: 123 };
        const securityTypeCollection: ISecurityType[] = [{ id: 456 }];
        expectedResult = service.addSecurityTypeToCollectionIfMissing(securityTypeCollection, securityType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityType);
      });

      it('should add only unique SecurityType to an array', () => {
        const securityTypeArray: ISecurityType[] = [{ id: 123 }, { id: 456 }, { id: 91812 }];
        const securityTypeCollection: ISecurityType[] = [{ id: 123 }];
        expectedResult = service.addSecurityTypeToCollectionIfMissing(securityTypeCollection, ...securityTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityType: ISecurityType = { id: 123 };
        const securityType2: ISecurityType = { id: 456 };
        expectedResult = service.addSecurityTypeToCollectionIfMissing([], securityType, securityType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityType);
        expect(expectedResult).toContain(securityType2);
      });

      it('should accept null and undefined values', () => {
        const securityType: ISecurityType = { id: 123 };
        expectedResult = service.addSecurityTypeToCollectionIfMissing([], null, securityType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityType);
      });

      it('should return initial array if no SecurityType is added', () => {
        const securityTypeCollection: ISecurityType[] = [{ id: 123 }];
        expectedResult = service.addSecurityTypeToCollectionIfMissing(securityTypeCollection, undefined, null);
        expect(expectedResult).toEqual(securityTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
