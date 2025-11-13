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

import { IPlaceholder, Placeholder } from '../placeholder.model';

import { PlaceholderService } from './placeholder.service';

describe('Placeholder Service', () => {
  let service: PlaceholderService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlaceholder;
  let expectedResult: IPlaceholder | IPlaceholder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlaceholderService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      token: 'AAAAAAA',
      fileUploadToken: 'AAAAAAA',
      compilationToken: 'AAAAAAA',
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

    it('should create a Placeholder', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Placeholder()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Placeholder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          token: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Placeholder', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          token: 'BBBBBB',
          compilationToken: 'BBBBBB',
        },
        new Placeholder()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Placeholder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          token: 'BBBBBB',
          fileUploadToken: 'BBBBBB',
          compilationToken: 'BBBBBB',
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

    it('should delete a Placeholder', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlaceholderToCollectionIfMissing', () => {
      it('should add a Placeholder to an empty array', () => {
        const placeholder: IPlaceholder = { id: 123 };
        expectedResult = service.addPlaceholderToCollectionIfMissing([], placeholder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(placeholder);
      });

      it('should not add a Placeholder to an array that contains it', () => {
        const placeholder: IPlaceholder = { id: 123 };
        const placeholderCollection: IPlaceholder[] = [
          {
            ...placeholder,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, placeholder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Placeholder to an array that doesn't contain it", () => {
        const placeholder: IPlaceholder = { id: 123 };
        const placeholderCollection: IPlaceholder[] = [{ id: 456 }];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, placeholder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(placeholder);
      });

      it('should add only unique Placeholder to an array', () => {
        const placeholderArray: IPlaceholder[] = [{ id: 123 }, { id: 456 }, { id: 86039 }];
        const placeholderCollection: IPlaceholder[] = [{ id: 123 }];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, ...placeholderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const placeholder: IPlaceholder = { id: 123 };
        const placeholder2: IPlaceholder = { id: 456 };
        expectedResult = service.addPlaceholderToCollectionIfMissing([], placeholder, placeholder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(placeholder);
        expect(expectedResult).toContain(placeholder2);
      });

      it('should accept null and undefined values', () => {
        const placeholder: IPlaceholder = { id: 123 };
        expectedResult = service.addPlaceholderToCollectionIfMissing([], null, placeholder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(placeholder);
      });

      it('should return initial array if no Placeholder is added', () => {
        const placeholderCollection: IPlaceholder[] = [{ id: 123 }];
        expectedResult = service.addPlaceholderToCollectionIfMissing(placeholderCollection, undefined, null);
        expect(expectedResult).toEqual(placeholderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
