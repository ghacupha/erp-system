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

import { IPartyRelationType, PartyRelationType } from '../party-relation-type.model';

import { PartyRelationTypeService } from './party-relation-type.service';

describe('PartyRelationType Service', () => {
  let service: PartyRelationTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IPartyRelationType;
  let expectedResult: IPartyRelationType | IPartyRelationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PartyRelationTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      partyRelationTypeCode: 'AAAAAAA',
      partyRelationType: 'AAAAAAA',
      partyRelationTypeDescription: 'AAAAAAA',
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

    it('should create a PartyRelationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PartyRelationType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyRelationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          partyRelationTypeCode: 'BBBBBB',
          partyRelationType: 'BBBBBB',
          partyRelationTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyRelationType', () => {
      const patchObject = Object.assign(
        {
          partyRelationTypeDescription: 'BBBBBB',
        },
        new PartyRelationType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyRelationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          partyRelationTypeCode: 'BBBBBB',
          partyRelationType: 'BBBBBB',
          partyRelationTypeDescription: 'BBBBBB',
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

    it('should delete a PartyRelationType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPartyRelationTypeToCollectionIfMissing', () => {
      it('should add a PartyRelationType to an empty array', () => {
        const partyRelationType: IPartyRelationType = { id: 123 };
        expectedResult = service.addPartyRelationTypeToCollectionIfMissing([], partyRelationType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partyRelationType);
      });

      it('should not add a PartyRelationType to an array that contains it', () => {
        const partyRelationType: IPartyRelationType = { id: 123 };
        const partyRelationTypeCollection: IPartyRelationType[] = [
          {
            ...partyRelationType,
          },
          { id: 456 },
        ];
        expectedResult = service.addPartyRelationTypeToCollectionIfMissing(partyRelationTypeCollection, partyRelationType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyRelationType to an array that doesn't contain it", () => {
        const partyRelationType: IPartyRelationType = { id: 123 };
        const partyRelationTypeCollection: IPartyRelationType[] = [{ id: 456 }];
        expectedResult = service.addPartyRelationTypeToCollectionIfMissing(partyRelationTypeCollection, partyRelationType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyRelationType);
      });

      it('should add only unique PartyRelationType to an array', () => {
        const partyRelationTypeArray: IPartyRelationType[] = [{ id: 123 }, { id: 456 }, { id: 48666 }];
        const partyRelationTypeCollection: IPartyRelationType[] = [{ id: 123 }];
        expectedResult = service.addPartyRelationTypeToCollectionIfMissing(partyRelationTypeCollection, ...partyRelationTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyRelationType: IPartyRelationType = { id: 123 };
        const partyRelationType2: IPartyRelationType = { id: 456 };
        expectedResult = service.addPartyRelationTypeToCollectionIfMissing([], partyRelationType, partyRelationType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyRelationType);
        expect(expectedResult).toContain(partyRelationType2);
      });

      it('should accept null and undefined values', () => {
        const partyRelationType: IPartyRelationType = { id: 123 };
        expectedResult = service.addPartyRelationTypeToCollectionIfMissing([], null, partyRelationType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partyRelationType);
      });

      it('should return initial array if no PartyRelationType is added', () => {
        const partyRelationTypeCollection: IPartyRelationType[] = [{ id: 123 }];
        expectedResult = service.addPartyRelationTypeToCollectionIfMissing(partyRelationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(partyRelationTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
