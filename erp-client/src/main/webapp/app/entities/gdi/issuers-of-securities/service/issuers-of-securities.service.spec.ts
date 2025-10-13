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

import { IIssuersOfSecurities, IssuersOfSecurities } from '../issuers-of-securities.model';

import { IssuersOfSecuritiesService } from './issuers-of-securities.service';

describe('IssuersOfSecurities Service', () => {
  let service: IssuersOfSecuritiesService;
  let httpMock: HttpTestingController;
  let elemDefault: IIssuersOfSecurities;
  let expectedResult: IIssuersOfSecurities | IIssuersOfSecurities[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IssuersOfSecuritiesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      issuerOfSecuritiesCode: 'AAAAAAA',
      issuerOfSecurities: 'AAAAAAA',
      issuerOfSecuritiesDescription: 'AAAAAAA',
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

    it('should create a IssuersOfSecurities', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new IssuersOfSecurities()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IssuersOfSecurities', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          issuerOfSecuritiesCode: 'BBBBBB',
          issuerOfSecurities: 'BBBBBB',
          issuerOfSecuritiesDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IssuersOfSecurities', () => {
      const patchObject = Object.assign(
        {
          issuerOfSecuritiesDescription: 'BBBBBB',
        },
        new IssuersOfSecurities()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IssuersOfSecurities', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          issuerOfSecuritiesCode: 'BBBBBB',
          issuerOfSecurities: 'BBBBBB',
          issuerOfSecuritiesDescription: 'BBBBBB',
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

    it('should delete a IssuersOfSecurities', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIssuersOfSecuritiesToCollectionIfMissing', () => {
      it('should add a IssuersOfSecurities to an empty array', () => {
        const issuersOfSecurities: IIssuersOfSecurities = { id: 123 };
        expectedResult = service.addIssuersOfSecuritiesToCollectionIfMissing([], issuersOfSecurities);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issuersOfSecurities);
      });

      it('should not add a IssuersOfSecurities to an array that contains it', () => {
        const issuersOfSecurities: IIssuersOfSecurities = { id: 123 };
        const issuersOfSecuritiesCollection: IIssuersOfSecurities[] = [
          {
            ...issuersOfSecurities,
          },
          { id: 456 },
        ];
        expectedResult = service.addIssuersOfSecuritiesToCollectionIfMissing(issuersOfSecuritiesCollection, issuersOfSecurities);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IssuersOfSecurities to an array that doesn't contain it", () => {
        const issuersOfSecurities: IIssuersOfSecurities = { id: 123 };
        const issuersOfSecuritiesCollection: IIssuersOfSecurities[] = [{ id: 456 }];
        expectedResult = service.addIssuersOfSecuritiesToCollectionIfMissing(issuersOfSecuritiesCollection, issuersOfSecurities);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issuersOfSecurities);
      });

      it('should add only unique IssuersOfSecurities to an array', () => {
        const issuersOfSecuritiesArray: IIssuersOfSecurities[] = [{ id: 123 }, { id: 456 }, { id: 78120 }];
        const issuersOfSecuritiesCollection: IIssuersOfSecurities[] = [{ id: 123 }];
        expectedResult = service.addIssuersOfSecuritiesToCollectionIfMissing(issuersOfSecuritiesCollection, ...issuersOfSecuritiesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const issuersOfSecurities: IIssuersOfSecurities = { id: 123 };
        const issuersOfSecurities2: IIssuersOfSecurities = { id: 456 };
        expectedResult = service.addIssuersOfSecuritiesToCollectionIfMissing([], issuersOfSecurities, issuersOfSecurities2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(issuersOfSecurities);
        expect(expectedResult).toContain(issuersOfSecurities2);
      });

      it('should accept null and undefined values', () => {
        const issuersOfSecurities: IIssuersOfSecurities = { id: 123 };
        expectedResult = service.addIssuersOfSecuritiesToCollectionIfMissing([], null, issuersOfSecurities, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(issuersOfSecurities);
      });

      it('should return initial array if no IssuersOfSecurities is added', () => {
        const issuersOfSecuritiesCollection: IIssuersOfSecurities[] = [{ id: 123 }];
        expectedResult = service.addIssuersOfSecuritiesToCollectionIfMissing(issuersOfSecuritiesCollection, undefined, null);
        expect(expectedResult).toEqual(issuersOfSecuritiesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
