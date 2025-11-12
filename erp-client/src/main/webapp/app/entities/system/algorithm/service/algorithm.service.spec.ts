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

import { IAlgorithm, Algorithm } from '../algorithm.model';

import { AlgorithmService } from './algorithm.service';

describe('Algorithm Service', () => {
  let service: AlgorithmService;
  let httpMock: HttpTestingController;
  let elemDefault: IAlgorithm;
  let expectedResult: IAlgorithm | IAlgorithm[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AlgorithmService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Algorithm', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Algorithm()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Algorithm', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Algorithm', () => {
      const patchObject = Object.assign({}, new Algorithm());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Algorithm', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Algorithm', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAlgorithmToCollectionIfMissing', () => {
      it('should add a Algorithm to an empty array', () => {
        const algorithm: IAlgorithm = { id: 123 };
        expectedResult = service.addAlgorithmToCollectionIfMissing([], algorithm);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(algorithm);
      });

      it('should not add a Algorithm to an array that contains it', () => {
        const algorithm: IAlgorithm = { id: 123 };
        const algorithmCollection: IAlgorithm[] = [
          {
            ...algorithm,
          },
          { id: 456 },
        ];
        expectedResult = service.addAlgorithmToCollectionIfMissing(algorithmCollection, algorithm);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Algorithm to an array that doesn't contain it", () => {
        const algorithm: IAlgorithm = { id: 123 };
        const algorithmCollection: IAlgorithm[] = [{ id: 456 }];
        expectedResult = service.addAlgorithmToCollectionIfMissing(algorithmCollection, algorithm);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(algorithm);
      });

      it('should add only unique Algorithm to an array', () => {
        const algorithmArray: IAlgorithm[] = [{ id: 123 }, { id: 456 }, { id: 38486 }];
        const algorithmCollection: IAlgorithm[] = [{ id: 123 }];
        expectedResult = service.addAlgorithmToCollectionIfMissing(algorithmCollection, ...algorithmArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const algorithm: IAlgorithm = { id: 123 };
        const algorithm2: IAlgorithm = { id: 456 };
        expectedResult = service.addAlgorithmToCollectionIfMissing([], algorithm, algorithm2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(algorithm);
        expect(expectedResult).toContain(algorithm2);
      });

      it('should accept null and undefined values', () => {
        const algorithm: IAlgorithm = { id: 123 };
        expectedResult = service.addAlgorithmToCollectionIfMissing([], null, algorithm, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(algorithm);
      });

      it('should return initial array if no Algorithm is added', () => {
        const algorithmCollection: IAlgorithm[] = [{ id: 123 }];
        expectedResult = service.addAlgorithmToCollectionIfMissing(algorithmCollection, undefined, null);
        expect(expectedResult).toEqual(algorithmCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
