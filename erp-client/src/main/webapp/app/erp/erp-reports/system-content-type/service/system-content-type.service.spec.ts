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

import { ISystemContentType, SystemContentType } from '../system-content-type.model';

import { SystemContentTypeService } from './system-content-type.service';
import { SystemContentTypeAvailability } from '../../../erp-common/enumerations/system-content-type-availability.model';

describe('SystemContentType Service', () => {
  let service: SystemContentTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISystemContentType;
  let expectedResult: ISystemContentType | ISystemContentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SystemContentTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      contentTypeName: 'AAAAAAA',
      contentTypeHeader: 'AAAAAAA',
      comments: 'AAAAAAA',
      availability: SystemContentTypeAvailability.SUPPORTED,
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

    it('should create a SystemContentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SystemContentType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemContentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          contentTypeName: 'BBBBBB',
          contentTypeHeader: 'BBBBBB',
          comments: 'BBBBBB',
          availability: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemContentType', () => {
      const patchObject = Object.assign(
        {
          contentTypeName: 'BBBBBB',
          comments: 'BBBBBB',
          availability: 'BBBBBB',
        },
        new SystemContentType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemContentType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          contentTypeName: 'BBBBBB',
          contentTypeHeader: 'BBBBBB',
          comments: 'BBBBBB',
          availability: 'BBBBBB',
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

    it('should delete a SystemContentType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSystemContentTypeToCollectionIfMissing', () => {
      it('should add a SystemContentType to an empty array', () => {
        const systemContentType: ISystemContentType = { id: 123 };
        expectedResult = service.addSystemContentTypeToCollectionIfMissing([], systemContentType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemContentType);
      });

      it('should not add a SystemContentType to an array that contains it', () => {
        const systemContentType: ISystemContentType = { id: 123 };
        const systemContentTypeCollection: ISystemContentType[] = [
          {
            ...systemContentType,
          },
          { id: 456 },
        ];
        expectedResult = service.addSystemContentTypeToCollectionIfMissing(systemContentTypeCollection, systemContentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemContentType to an array that doesn't contain it", () => {
        const systemContentType: ISystemContentType = { id: 123 };
        const systemContentTypeCollection: ISystemContentType[] = [{ id: 456 }];
        expectedResult = service.addSystemContentTypeToCollectionIfMissing(systemContentTypeCollection, systemContentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemContentType);
      });

      it('should add only unique SystemContentType to an array', () => {
        const systemContentTypeArray: ISystemContentType[] = [{ id: 123 }, { id: 456 }, { id: 28135 }];
        const systemContentTypeCollection: ISystemContentType[] = [{ id: 123 }];
        expectedResult = service.addSystemContentTypeToCollectionIfMissing(systemContentTypeCollection, ...systemContentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemContentType: ISystemContentType = { id: 123 };
        const systemContentType2: ISystemContentType = { id: 456 };
        expectedResult = service.addSystemContentTypeToCollectionIfMissing([], systemContentType, systemContentType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemContentType);
        expect(expectedResult).toContain(systemContentType2);
      });

      it('should accept null and undefined values', () => {
        const systemContentType: ISystemContentType = { id: 123 };
        expectedResult = service.addSystemContentTypeToCollectionIfMissing([], null, systemContentType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemContentType);
      });

      it('should return initial array if no SystemContentType is added', () => {
        const systemContentTypeCollection: ISystemContentType[] = [{ id: 123 }];
        expectedResult = service.addSystemContentTypeToCollectionIfMissing(systemContentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(systemContentTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
