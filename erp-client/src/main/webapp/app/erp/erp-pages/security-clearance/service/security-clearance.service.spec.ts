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

import { ISecurityClearance, SecurityClearance } from '../security-clearance.model';

import { SecurityClearanceService } from './security-clearance.service';

describe('SecurityClearance Service', () => {
  let service: SecurityClearanceService;
  let httpMock: HttpTestingController;
  let elemDefault: ISecurityClearance;
  let expectedResult: ISecurityClearance | ISecurityClearance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityClearanceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      clearanceLevel: 'AAAAAAA',
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

    it('should create a SecurityClearance', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SecurityClearance()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityClearance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          clearanceLevel: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SecurityClearance', () => {
      const patchObject = Object.assign({}, new SecurityClearance());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityClearance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          clearanceLevel: 'BBBBBB',
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

    it('should delete a SecurityClearance', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSecurityClearanceToCollectionIfMissing', () => {
      it('should add a SecurityClearance to an empty array', () => {
        const securityClearance: ISecurityClearance = { id: 123 };
        expectedResult = service.addSecurityClearanceToCollectionIfMissing([], securityClearance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityClearance);
      });

      it('should not add a SecurityClearance to an array that contains it', () => {
        const securityClearance: ISecurityClearance = { id: 123 };
        const securityClearanceCollection: ISecurityClearance[] = [
          {
            ...securityClearance,
          },
          { id: 456 },
        ];
        expectedResult = service.addSecurityClearanceToCollectionIfMissing(securityClearanceCollection, securityClearance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityClearance to an array that doesn't contain it", () => {
        const securityClearance: ISecurityClearance = { id: 123 };
        const securityClearanceCollection: ISecurityClearance[] = [{ id: 456 }];
        expectedResult = service.addSecurityClearanceToCollectionIfMissing(securityClearanceCollection, securityClearance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityClearance);
      });

      it('should add only unique SecurityClearance to an array', () => {
        const securityClearanceArray: ISecurityClearance[] = [{ id: 123 }, { id: 456 }, { id: 60756 }];
        const securityClearanceCollection: ISecurityClearance[] = [{ id: 123 }];
        expectedResult = service.addSecurityClearanceToCollectionIfMissing(securityClearanceCollection, ...securityClearanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityClearance: ISecurityClearance = { id: 123 };
        const securityClearance2: ISecurityClearance = { id: 456 };
        expectedResult = service.addSecurityClearanceToCollectionIfMissing([], securityClearance, securityClearance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityClearance);
        expect(expectedResult).toContain(securityClearance2);
      });

      it('should accept null and undefined values', () => {
        const securityClearance: ISecurityClearance = { id: 123 };
        expectedResult = service.addSecurityClearanceToCollectionIfMissing([], null, securityClearance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityClearance);
      });

      it('should return initial array if no SecurityClearance is added', () => {
        const securityClearanceCollection: ISecurityClearance[] = [{ id: 123 }];
        expectedResult = service.addSecurityClearanceToCollectionIfMissing(securityClearanceCollection, undefined, null);
        expect(expectedResult).toEqual(securityClearanceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
