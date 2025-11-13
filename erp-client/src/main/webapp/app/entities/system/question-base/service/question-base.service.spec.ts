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

import { ControlTypes } from 'app/entities/enumerations/control-types.model';
import { IQuestionBase, QuestionBase } from '../question-base.model';

import { QuestionBaseService } from './question-base.service';

describe('QuestionBase Service', () => {
  let service: QuestionBaseService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuestionBase;
  let expectedResult: IQuestionBase | IQuestionBase[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionBaseService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      context: 'AAAAAAA',
      serial: 'AAAAAAA',
      questionBaseValue: 'AAAAAAA',
      questionBaseKey: 'AAAAAAA',
      questionBaseLabel: 'AAAAAAA',
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

    it('should create a QuestionBase', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new QuestionBase()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuestionBase', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          context: 'BBBBBB',
          serial: 'BBBBBB',
          questionBaseValue: 'BBBBBB',
          questionBaseKey: 'BBBBBB',
          questionBaseLabel: 'BBBBBB',
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

    it('should partial update a QuestionBase', () => {
      const patchObject = Object.assign(
        {
          context: 'BBBBBB',
          questionBaseValue: 'BBBBBB',
          questionBaseKey: 'BBBBBB',
          questionBaseLabel: 'BBBBBB',
        },
        new QuestionBase()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuestionBase', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          context: 'BBBBBB',
          serial: 'BBBBBB',
          questionBaseValue: 'BBBBBB',
          questionBaseKey: 'BBBBBB',
          questionBaseLabel: 'BBBBBB',
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

    it('should delete a QuestionBase', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuestionBaseToCollectionIfMissing', () => {
      it('should add a QuestionBase to an empty array', () => {
        const questionBase: IQuestionBase = { id: 123 };
        expectedResult = service.addQuestionBaseToCollectionIfMissing([], questionBase);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionBase);
      });

      it('should not add a QuestionBase to an array that contains it', () => {
        const questionBase: IQuestionBase = { id: 123 };
        const questionBaseCollection: IQuestionBase[] = [
          {
            ...questionBase,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuestionBaseToCollectionIfMissing(questionBaseCollection, questionBase);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuestionBase to an array that doesn't contain it", () => {
        const questionBase: IQuestionBase = { id: 123 };
        const questionBaseCollection: IQuestionBase[] = [{ id: 456 }];
        expectedResult = service.addQuestionBaseToCollectionIfMissing(questionBaseCollection, questionBase);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionBase);
      });

      it('should add only unique QuestionBase to an array', () => {
        const questionBaseArray: IQuestionBase[] = [{ id: 123 }, { id: 456 }, { id: 86481 }];
        const questionBaseCollection: IQuestionBase[] = [{ id: 123 }];
        expectedResult = service.addQuestionBaseToCollectionIfMissing(questionBaseCollection, ...questionBaseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionBase: IQuestionBase = { id: 123 };
        const questionBase2: IQuestionBase = { id: 456 };
        expectedResult = service.addQuestionBaseToCollectionIfMissing([], questionBase, questionBase2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionBase);
        expect(expectedResult).toContain(questionBase2);
      });

      it('should accept null and undefined values', () => {
        const questionBase: IQuestionBase = { id: 123 };
        expectedResult = service.addQuestionBaseToCollectionIfMissing([], null, questionBase, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionBase);
      });

      it('should return initial array if no QuestionBase is added', () => {
        const questionBaseCollection: IQuestionBase[] = [{ id: 123 }];
        expectedResult = service.addQuestionBaseToCollectionIfMissing(questionBaseCollection, undefined, null);
        expect(expectedResult).toEqual(questionBaseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
