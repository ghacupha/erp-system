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

import { ISecurityClassificationType, SecurityClassificationType } from '../security-classification-type.model';

import { SecurityClassificationTypeService } from './security-classification-type.service';

describe('SecurityClassificationType Service', () => {
  let service: SecurityClassificationTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISecurityClassificationType;
  let expectedResult: ISecurityClassificationType | ISecurityClassificationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityClassificationTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      securityClassificationTypeCode: 'AAAAAAA',
      securityClassificationType: 'AAAAAAA',
      securityClassificationDetails: 'AAAAAAA',
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

    it('should create a SecurityClassificationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SecurityClassificationType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityClassificationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          securityClassificationTypeCode: 'BBBBBB',
          securityClassificationType: 'BBBBBB',
          securityClassificationDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SecurityClassificationType', () => {
      const patchObject = Object.assign(
        {
          securityClassificationDetails: 'BBBBBB',
        },
        new SecurityClassificationType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityClassificationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          securityClassificationTypeCode: 'BBBBBB',
          securityClassificationType: 'BBBBBB',
          securityClassificationDetails: 'BBBBBB',
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

    it('should delete a SecurityClassificationType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSecurityClassificationTypeToCollectionIfMissing', () => {
      it('should add a SecurityClassificationType to an empty array', () => {
        const securityClassificationType: ISecurityClassificationType = { id: 123 };
        expectedResult = service.addSecurityClassificationTypeToCollectionIfMissing([], securityClassificationType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityClassificationType);
      });

      it('should not add a SecurityClassificationType to an array that contains it', () => {
        const securityClassificationType: ISecurityClassificationType = { id: 123 };
        const securityClassificationTypeCollection: ISecurityClassificationType[] = [
          {
            ...securityClassificationType,
          },
          { id: 456 },
        ];
        expectedResult = service.addSecurityClassificationTypeToCollectionIfMissing(
          securityClassificationTypeCollection,
          securityClassificationType
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityClassificationType to an array that doesn't contain it", () => {
        const securityClassificationType: ISecurityClassificationType = { id: 123 };
        const securityClassificationTypeCollection: ISecurityClassificationType[] = [{ id: 456 }];
        expectedResult = service.addSecurityClassificationTypeToCollectionIfMissing(
          securityClassificationTypeCollection,
          securityClassificationType
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityClassificationType);
      });

      it('should add only unique SecurityClassificationType to an array', () => {
        const securityClassificationTypeArray: ISecurityClassificationType[] = [{ id: 123 }, { id: 456 }, { id: 42932 }];
        const securityClassificationTypeCollection: ISecurityClassificationType[] = [{ id: 123 }];
        expectedResult = service.addSecurityClassificationTypeToCollectionIfMissing(
          securityClassificationTypeCollection,
          ...securityClassificationTypeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityClassificationType: ISecurityClassificationType = { id: 123 };
        const securityClassificationType2: ISecurityClassificationType = { id: 456 };
        expectedResult = service.addSecurityClassificationTypeToCollectionIfMissing(
          [],
          securityClassificationType,
          securityClassificationType2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityClassificationType);
        expect(expectedResult).toContain(securityClassificationType2);
      });

      it('should accept null and undefined values', () => {
        const securityClassificationType: ISecurityClassificationType = { id: 123 };
        expectedResult = service.addSecurityClassificationTypeToCollectionIfMissing([], null, securityClassificationType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityClassificationType);
      });

      it('should return initial array if no SecurityClassificationType is added', () => {
        const securityClassificationTypeCollection: ISecurityClassificationType[] = [{ id: 123 }];
        expectedResult = service.addSecurityClassificationTypeToCollectionIfMissing(securityClassificationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(securityClassificationTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
