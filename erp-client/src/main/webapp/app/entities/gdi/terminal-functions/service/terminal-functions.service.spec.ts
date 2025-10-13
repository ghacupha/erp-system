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

import { ITerminalFunctions, TerminalFunctions } from '../terminal-functions.model';

import { TerminalFunctionsService } from './terminal-functions.service';

describe('TerminalFunctions Service', () => {
  let service: TerminalFunctionsService;
  let httpMock: HttpTestingController;
  let elemDefault: ITerminalFunctions;
  let expectedResult: ITerminalFunctions | ITerminalFunctions[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TerminalFunctionsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      functionCode: 'AAAAAAA',
      terminalFunctionality: 'AAAAAAA',
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

    it('should create a TerminalFunctions', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TerminalFunctions()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TerminalFunctions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          functionCode: 'BBBBBB',
          terminalFunctionality: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TerminalFunctions', () => {
      const patchObject = Object.assign(
        {
          functionCode: 'BBBBBB',
        },
        new TerminalFunctions()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TerminalFunctions', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          functionCode: 'BBBBBB',
          terminalFunctionality: 'BBBBBB',
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

    it('should delete a TerminalFunctions', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTerminalFunctionsToCollectionIfMissing', () => {
      it('should add a TerminalFunctions to an empty array', () => {
        const terminalFunctions: ITerminalFunctions = { id: 123 };
        expectedResult = service.addTerminalFunctionsToCollectionIfMissing([], terminalFunctions);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminalFunctions);
      });

      it('should not add a TerminalFunctions to an array that contains it', () => {
        const terminalFunctions: ITerminalFunctions = { id: 123 };
        const terminalFunctionsCollection: ITerminalFunctions[] = [
          {
            ...terminalFunctions,
          },
          { id: 456 },
        ];
        expectedResult = service.addTerminalFunctionsToCollectionIfMissing(terminalFunctionsCollection, terminalFunctions);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TerminalFunctions to an array that doesn't contain it", () => {
        const terminalFunctions: ITerminalFunctions = { id: 123 };
        const terminalFunctionsCollection: ITerminalFunctions[] = [{ id: 456 }];
        expectedResult = service.addTerminalFunctionsToCollectionIfMissing(terminalFunctionsCollection, terminalFunctions);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminalFunctions);
      });

      it('should add only unique TerminalFunctions to an array', () => {
        const terminalFunctionsArray: ITerminalFunctions[] = [{ id: 123 }, { id: 456 }, { id: 43501 }];
        const terminalFunctionsCollection: ITerminalFunctions[] = [{ id: 123 }];
        expectedResult = service.addTerminalFunctionsToCollectionIfMissing(terminalFunctionsCollection, ...terminalFunctionsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const terminalFunctions: ITerminalFunctions = { id: 123 };
        const terminalFunctions2: ITerminalFunctions = { id: 456 };
        expectedResult = service.addTerminalFunctionsToCollectionIfMissing([], terminalFunctions, terminalFunctions2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminalFunctions);
        expect(expectedResult).toContain(terminalFunctions2);
      });

      it('should accept null and undefined values', () => {
        const terminalFunctions: ITerminalFunctions = { id: 123 };
        expectedResult = service.addTerminalFunctionsToCollectionIfMissing([], null, terminalFunctions, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminalFunctions);
      });

      it('should return initial array if no TerminalFunctions is added', () => {
        const terminalFunctionsCollection: ITerminalFunctions[] = [{ id: 123 }];
        expectedResult = service.addTerminalFunctionsToCollectionIfMissing(terminalFunctionsCollection, undefined, null);
        expect(expectedResult).toEqual(terminalFunctionsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
