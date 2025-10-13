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

import { ITerminalTypes, TerminalTypes } from '../terminal-types.model';

import { TerminalTypesService } from './terminal-types.service';

describe('TerminalTypes Service', () => {
  let service: TerminalTypesService;
  let httpMock: HttpTestingController;
  let elemDefault: ITerminalTypes;
  let expectedResult: ITerminalTypes | ITerminalTypes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TerminalTypesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      txnTerminalTypeCode: 'AAAAAAA',
      txnChannelType: 'AAAAAAA',
      txnChannelTypeDetails: 'AAAAAAA',
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

    it('should create a TerminalTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TerminalTypes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TerminalTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          txnTerminalTypeCode: 'BBBBBB',
          txnChannelType: 'BBBBBB',
          txnChannelTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TerminalTypes', () => {
      const patchObject = Object.assign({}, new TerminalTypes());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TerminalTypes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          txnTerminalTypeCode: 'BBBBBB',
          txnChannelType: 'BBBBBB',
          txnChannelTypeDetails: 'BBBBBB',
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

    it('should delete a TerminalTypes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTerminalTypesToCollectionIfMissing', () => {
      it('should add a TerminalTypes to an empty array', () => {
        const terminalTypes: ITerminalTypes = { id: 123 };
        expectedResult = service.addTerminalTypesToCollectionIfMissing([], terminalTypes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminalTypes);
      });

      it('should not add a TerminalTypes to an array that contains it', () => {
        const terminalTypes: ITerminalTypes = { id: 123 };
        const terminalTypesCollection: ITerminalTypes[] = [
          {
            ...terminalTypes,
          },
          { id: 456 },
        ];
        expectedResult = service.addTerminalTypesToCollectionIfMissing(terminalTypesCollection, terminalTypes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TerminalTypes to an array that doesn't contain it", () => {
        const terminalTypes: ITerminalTypes = { id: 123 };
        const terminalTypesCollection: ITerminalTypes[] = [{ id: 456 }];
        expectedResult = service.addTerminalTypesToCollectionIfMissing(terminalTypesCollection, terminalTypes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminalTypes);
      });

      it('should add only unique TerminalTypes to an array', () => {
        const terminalTypesArray: ITerminalTypes[] = [{ id: 123 }, { id: 456 }, { id: 99281 }];
        const terminalTypesCollection: ITerminalTypes[] = [{ id: 123 }];
        expectedResult = service.addTerminalTypesToCollectionIfMissing(terminalTypesCollection, ...terminalTypesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const terminalTypes: ITerminalTypes = { id: 123 };
        const terminalTypes2: ITerminalTypes = { id: 456 };
        expectedResult = service.addTerminalTypesToCollectionIfMissing([], terminalTypes, terminalTypes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminalTypes);
        expect(expectedResult).toContain(terminalTypes2);
      });

      it('should accept null and undefined values', () => {
        const terminalTypes: ITerminalTypes = { id: 123 };
        expectedResult = service.addTerminalTypesToCollectionIfMissing([], null, terminalTypes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminalTypes);
      });

      it('should return initial array if no TerminalTypes is added', () => {
        const terminalTypesCollection: ITerminalTypes[] = [{ id: 123 }];
        expectedResult = service.addTerminalTypesToCollectionIfMissing(terminalTypesCollection, undefined, null);
        expect(expectedResult).toEqual(terminalTypesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
