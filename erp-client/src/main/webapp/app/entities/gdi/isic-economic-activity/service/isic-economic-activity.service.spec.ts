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

import { IIsicEconomicActivity, IsicEconomicActivity } from '../isic-economic-activity.model';

import { IsicEconomicActivityService } from './isic-economic-activity.service';

describe('IsicEconomicActivity Service', () => {
  let service: IsicEconomicActivityService;
  let httpMock: HttpTestingController;
  let elemDefault: IIsicEconomicActivity;
  let expectedResult: IIsicEconomicActivity | IIsicEconomicActivity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IsicEconomicActivityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      businessEconomicActivityCode: 'AAAAAAA',
      section: 'AAAAAAA',
      sectionLabel: 'AAAAAAA',
      division: 'AAAAAAA',
      divisionLabel: 'AAAAAAA',
      groupCode: 'AAAAAAA',
      groupLabel: 'AAAAAAA',
      classCode: 'AAAAAAA',
      businessEconomicActivityType: 'AAAAAAA',
      businessEconomicActivityTypeDescription: 'AAAAAAA',
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

    it('should create a IsicEconomicActivity', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new IsicEconomicActivity()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IsicEconomicActivity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessEconomicActivityCode: 'BBBBBB',
          section: 'BBBBBB',
          sectionLabel: 'BBBBBB',
          division: 'BBBBBB',
          divisionLabel: 'BBBBBB',
          groupCode: 'BBBBBB',
          groupLabel: 'BBBBBB',
          classCode: 'BBBBBB',
          businessEconomicActivityType: 'BBBBBB',
          businessEconomicActivityTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IsicEconomicActivity', () => {
      const patchObject = Object.assign(
        {
          sectionLabel: 'BBBBBB',
          divisionLabel: 'BBBBBB',
          groupLabel: 'BBBBBB',
          businessEconomicActivityTypeDescription: 'BBBBBB',
        },
        new IsicEconomicActivity()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IsicEconomicActivity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          businessEconomicActivityCode: 'BBBBBB',
          section: 'BBBBBB',
          sectionLabel: 'BBBBBB',
          division: 'BBBBBB',
          divisionLabel: 'BBBBBB',
          groupCode: 'BBBBBB',
          groupLabel: 'BBBBBB',
          classCode: 'BBBBBB',
          businessEconomicActivityType: 'BBBBBB',
          businessEconomicActivityTypeDescription: 'BBBBBB',
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

    it('should delete a IsicEconomicActivity', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIsicEconomicActivityToCollectionIfMissing', () => {
      it('should add a IsicEconomicActivity to an empty array', () => {
        const isicEconomicActivity: IIsicEconomicActivity = { id: 123 };
        expectedResult = service.addIsicEconomicActivityToCollectionIfMissing([], isicEconomicActivity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isicEconomicActivity);
      });

      it('should not add a IsicEconomicActivity to an array that contains it', () => {
        const isicEconomicActivity: IIsicEconomicActivity = { id: 123 };
        const isicEconomicActivityCollection: IIsicEconomicActivity[] = [
          {
            ...isicEconomicActivity,
          },
          { id: 456 },
        ];
        expectedResult = service.addIsicEconomicActivityToCollectionIfMissing(isicEconomicActivityCollection, isicEconomicActivity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IsicEconomicActivity to an array that doesn't contain it", () => {
        const isicEconomicActivity: IIsicEconomicActivity = { id: 123 };
        const isicEconomicActivityCollection: IIsicEconomicActivity[] = [{ id: 456 }];
        expectedResult = service.addIsicEconomicActivityToCollectionIfMissing(isicEconomicActivityCollection, isicEconomicActivity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isicEconomicActivity);
      });

      it('should add only unique IsicEconomicActivity to an array', () => {
        const isicEconomicActivityArray: IIsicEconomicActivity[] = [{ id: 123 }, { id: 456 }, { id: 55802 }];
        const isicEconomicActivityCollection: IIsicEconomicActivity[] = [{ id: 123 }];
        expectedResult = service.addIsicEconomicActivityToCollectionIfMissing(isicEconomicActivityCollection, ...isicEconomicActivityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const isicEconomicActivity: IIsicEconomicActivity = { id: 123 };
        const isicEconomicActivity2: IIsicEconomicActivity = { id: 456 };
        expectedResult = service.addIsicEconomicActivityToCollectionIfMissing([], isicEconomicActivity, isicEconomicActivity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(isicEconomicActivity);
        expect(expectedResult).toContain(isicEconomicActivity2);
      });

      it('should accept null and undefined values', () => {
        const isicEconomicActivity: IIsicEconomicActivity = { id: 123 };
        expectedResult = service.addIsicEconomicActivityToCollectionIfMissing([], null, isicEconomicActivity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(isicEconomicActivity);
      });

      it('should return initial array if no IsicEconomicActivity is added', () => {
        const isicEconomicActivityCollection: IIsicEconomicActivity[] = [{ id: 123 }];
        expectedResult = service.addIsicEconomicActivityToCollectionIfMissing(isicEconomicActivityCollection, undefined, null);
        expect(expectedResult).toEqual(isicEconomicActivityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
