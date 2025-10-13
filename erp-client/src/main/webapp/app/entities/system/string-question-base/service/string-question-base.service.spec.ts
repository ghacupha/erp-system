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

import { ControlTypes } from 'app/entities/enumerations/control-types.model';
import { IStringQuestionBase, StringQuestionBase } from '../string-question-base.model';

import { StringQuestionBaseService } from './string-question-base.service';

describe('StringQuestionBase Service', () => {
  let service: StringQuestionBaseService;
  let httpMock: HttpTestingController;
  let elemDefault: IStringQuestionBase;
  let expectedResult: IStringQuestionBase | IStringQuestionBase[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StringQuestionBaseService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      value: 'AAAAAAA',
      key: 'AAAAAAA',
      label: 'AAAAAAA',
      required: false,
      order: 0,
      controlType: ControlTypes.TEXTBOX,
      placeholder: 'AAAAAAA',
      iterable: false,
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

    it('should create a StringQuestionBase', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new StringQuestionBase()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StringQuestionBase', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          value: 'BBBBBB',
          key: 'BBBBBB',
          label: 'BBBBBB',
          required: true,
          order: 1,
          controlType: 'BBBBBB',
          placeholder: 'BBBBBB',
          iterable: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StringQuestionBase', () => {
      const patchObject = Object.assign(
        {
          key: 'BBBBBB',
          order: 1,
          controlType: 'BBBBBB',
          placeholder: 'BBBBBB',
          iterable: true,
        },
        new StringQuestionBase()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StringQuestionBase', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          value: 'BBBBBB',
          key: 'BBBBBB',
          label: 'BBBBBB',
          required: true,
          order: 1,
          controlType: 'BBBBBB',
          placeholder: 'BBBBBB',
          iterable: true,
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

    it('should delete a StringQuestionBase', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStringQuestionBaseToCollectionIfMissing', () => {
      it('should add a StringQuestionBase to an empty array', () => {
        const stringQuestionBase: IStringQuestionBase = { id: 123 };
        expectedResult = service.addStringQuestionBaseToCollectionIfMissing([], stringQuestionBase);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stringQuestionBase);
      });

      it('should not add a StringQuestionBase to an array that contains it', () => {
        const stringQuestionBase: IStringQuestionBase = { id: 123 };
        const stringQuestionBaseCollection: IStringQuestionBase[] = [
          {
            ...stringQuestionBase,
          },
          { id: 456 },
        ];
        expectedResult = service.addStringQuestionBaseToCollectionIfMissing(stringQuestionBaseCollection, stringQuestionBase);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StringQuestionBase to an array that doesn't contain it", () => {
        const stringQuestionBase: IStringQuestionBase = { id: 123 };
        const stringQuestionBaseCollection: IStringQuestionBase[] = [{ id: 456 }];
        expectedResult = service.addStringQuestionBaseToCollectionIfMissing(stringQuestionBaseCollection, stringQuestionBase);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stringQuestionBase);
      });

      it('should add only unique StringQuestionBase to an array', () => {
        const stringQuestionBaseArray: IStringQuestionBase[] = [{ id: 123 }, { id: 456 }, { id: 45954 }];
        const stringQuestionBaseCollection: IStringQuestionBase[] = [{ id: 123 }];
        expectedResult = service.addStringQuestionBaseToCollectionIfMissing(stringQuestionBaseCollection, ...stringQuestionBaseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stringQuestionBase: IStringQuestionBase = { id: 123 };
        const stringQuestionBase2: IStringQuestionBase = { id: 456 };
        expectedResult = service.addStringQuestionBaseToCollectionIfMissing([], stringQuestionBase, stringQuestionBase2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stringQuestionBase);
        expect(expectedResult).toContain(stringQuestionBase2);
      });

      it('should accept null and undefined values', () => {
        const stringQuestionBase: IStringQuestionBase = { id: 123 };
        expectedResult = service.addStringQuestionBaseToCollectionIfMissing([], null, stringQuestionBase, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stringQuestionBase);
      });

      it('should return initial array if no StringQuestionBase is added', () => {
        const stringQuestionBaseCollection: IStringQuestionBase[] = [{ id: 123 }];
        expectedResult = service.addStringQuestionBaseToCollectionIfMissing(stringQuestionBaseCollection, undefined, null);
        expect(expectedResult).toEqual(stringQuestionBaseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
