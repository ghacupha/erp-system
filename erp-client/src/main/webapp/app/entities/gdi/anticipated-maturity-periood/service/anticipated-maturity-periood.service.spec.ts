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

import { IAnticipatedMaturityPeriood, AnticipatedMaturityPeriood } from '../anticipated-maturity-periood.model';

import { AnticipatedMaturityPerioodService } from './anticipated-maturity-periood.service';

describe('AnticipatedMaturityPeriood Service', () => {
  let service: AnticipatedMaturityPerioodService;
  let httpMock: HttpTestingController;
  let elemDefault: IAnticipatedMaturityPeriood;
  let expectedResult: IAnticipatedMaturityPeriood | IAnticipatedMaturityPeriood[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AnticipatedMaturityPerioodService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      anticipatedMaturityTenorCode: 'AAAAAAA',
      aniticipatedMaturityTenorType: 'AAAAAAA',
      anticipatedMaturityTenorDetails: 'AAAAAAA',
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

    it('should create a AnticipatedMaturityPeriood', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AnticipatedMaturityPeriood()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AnticipatedMaturityPeriood', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          anticipatedMaturityTenorCode: 'BBBBBB',
          aniticipatedMaturityTenorType: 'BBBBBB',
          anticipatedMaturityTenorDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AnticipatedMaturityPeriood', () => {
      const patchObject = Object.assign(
        {
          anticipatedMaturityTenorCode: 'BBBBBB',
          aniticipatedMaturityTenorType: 'BBBBBB',
        },
        new AnticipatedMaturityPeriood()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AnticipatedMaturityPeriood', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          anticipatedMaturityTenorCode: 'BBBBBB',
          aniticipatedMaturityTenorType: 'BBBBBB',
          anticipatedMaturityTenorDetails: 'BBBBBB',
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

    it('should delete a AnticipatedMaturityPeriood', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAnticipatedMaturityPerioodToCollectionIfMissing', () => {
      it('should add a AnticipatedMaturityPeriood to an empty array', () => {
        const anticipatedMaturityPeriood: IAnticipatedMaturityPeriood = { id: 123 };
        expectedResult = service.addAnticipatedMaturityPerioodToCollectionIfMissing([], anticipatedMaturityPeriood);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(anticipatedMaturityPeriood);
      });

      it('should not add a AnticipatedMaturityPeriood to an array that contains it', () => {
        const anticipatedMaturityPeriood: IAnticipatedMaturityPeriood = { id: 123 };
        const anticipatedMaturityPerioodCollection: IAnticipatedMaturityPeriood[] = [
          {
            ...anticipatedMaturityPeriood,
          },
          { id: 456 },
        ];
        expectedResult = service.addAnticipatedMaturityPerioodToCollectionIfMissing(
          anticipatedMaturityPerioodCollection,
          anticipatedMaturityPeriood
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AnticipatedMaturityPeriood to an array that doesn't contain it", () => {
        const anticipatedMaturityPeriood: IAnticipatedMaturityPeriood = { id: 123 };
        const anticipatedMaturityPerioodCollection: IAnticipatedMaturityPeriood[] = [{ id: 456 }];
        expectedResult = service.addAnticipatedMaturityPerioodToCollectionIfMissing(
          anticipatedMaturityPerioodCollection,
          anticipatedMaturityPeriood
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(anticipatedMaturityPeriood);
      });

      it('should add only unique AnticipatedMaturityPeriood to an array', () => {
        const anticipatedMaturityPerioodArray: IAnticipatedMaturityPeriood[] = [{ id: 123 }, { id: 456 }, { id: 57426 }];
        const anticipatedMaturityPerioodCollection: IAnticipatedMaturityPeriood[] = [{ id: 123 }];
        expectedResult = service.addAnticipatedMaturityPerioodToCollectionIfMissing(
          anticipatedMaturityPerioodCollection,
          ...anticipatedMaturityPerioodArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const anticipatedMaturityPeriood: IAnticipatedMaturityPeriood = { id: 123 };
        const anticipatedMaturityPeriood2: IAnticipatedMaturityPeriood = { id: 456 };
        expectedResult = service.addAnticipatedMaturityPerioodToCollectionIfMissing(
          [],
          anticipatedMaturityPeriood,
          anticipatedMaturityPeriood2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(anticipatedMaturityPeriood);
        expect(expectedResult).toContain(anticipatedMaturityPeriood2);
      });

      it('should accept null and undefined values', () => {
        const anticipatedMaturityPeriood: IAnticipatedMaturityPeriood = { id: 123 };
        expectedResult = service.addAnticipatedMaturityPerioodToCollectionIfMissing([], null, anticipatedMaturityPeriood, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(anticipatedMaturityPeriood);
      });

      it('should return initial array if no AnticipatedMaturityPeriood is added', () => {
        const anticipatedMaturityPerioodCollection: IAnticipatedMaturityPeriood[] = [{ id: 123 }];
        expectedResult = service.addAnticipatedMaturityPerioodToCollectionIfMissing(anticipatedMaturityPerioodCollection, undefined, null);
        expect(expectedResult).toEqual(anticipatedMaturityPerioodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
