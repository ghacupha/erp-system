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

import { ICrbNatureOfInformation, CrbNatureOfInformation } from '../crb-nature-of-information.model';

import { CrbNatureOfInformationService } from './crb-nature-of-information.service';

describe('CrbNatureOfInformation Service', () => {
  let service: CrbNatureOfInformationService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbNatureOfInformation;
  let expectedResult: ICrbNatureOfInformation | ICrbNatureOfInformation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbNatureOfInformationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      natureOfInformationTypeCode: 'AAAAAAA',
      natureOfInformationType: 'AAAAAAA',
      natureOfInformationTypeDescription: 'AAAAAAA',
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

    it('should create a CrbNatureOfInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbNatureOfInformation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbNatureOfInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          natureOfInformationTypeCode: 'BBBBBB',
          natureOfInformationType: 'BBBBBB',
          natureOfInformationTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbNatureOfInformation', () => {
      const patchObject = Object.assign(
        {
          natureOfInformationTypeCode: 'BBBBBB',
          natureOfInformationTypeDescription: 'BBBBBB',
        },
        new CrbNatureOfInformation()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbNatureOfInformation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          natureOfInformationTypeCode: 'BBBBBB',
          natureOfInformationType: 'BBBBBB',
          natureOfInformationTypeDescription: 'BBBBBB',
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

    it('should delete a CrbNatureOfInformation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbNatureOfInformationToCollectionIfMissing', () => {
      it('should add a CrbNatureOfInformation to an empty array', () => {
        const crbNatureOfInformation: ICrbNatureOfInformation = { id: 123 };
        expectedResult = service.addCrbNatureOfInformationToCollectionIfMissing([], crbNatureOfInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbNatureOfInformation);
      });

      it('should not add a CrbNatureOfInformation to an array that contains it', () => {
        const crbNatureOfInformation: ICrbNatureOfInformation = { id: 123 };
        const crbNatureOfInformationCollection: ICrbNatureOfInformation[] = [
          {
            ...crbNatureOfInformation,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbNatureOfInformationToCollectionIfMissing(crbNatureOfInformationCollection, crbNatureOfInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbNatureOfInformation to an array that doesn't contain it", () => {
        const crbNatureOfInformation: ICrbNatureOfInformation = { id: 123 };
        const crbNatureOfInformationCollection: ICrbNatureOfInformation[] = [{ id: 456 }];
        expectedResult = service.addCrbNatureOfInformationToCollectionIfMissing(crbNatureOfInformationCollection, crbNatureOfInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbNatureOfInformation);
      });

      it('should add only unique CrbNatureOfInformation to an array', () => {
        const crbNatureOfInformationArray: ICrbNatureOfInformation[] = [{ id: 123 }, { id: 456 }, { id: 10427 }];
        const crbNatureOfInformationCollection: ICrbNatureOfInformation[] = [{ id: 123 }];
        expectedResult = service.addCrbNatureOfInformationToCollectionIfMissing(
          crbNatureOfInformationCollection,
          ...crbNatureOfInformationArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbNatureOfInformation: ICrbNatureOfInformation = { id: 123 };
        const crbNatureOfInformation2: ICrbNatureOfInformation = { id: 456 };
        expectedResult = service.addCrbNatureOfInformationToCollectionIfMissing([], crbNatureOfInformation, crbNatureOfInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbNatureOfInformation);
        expect(expectedResult).toContain(crbNatureOfInformation2);
      });

      it('should accept null and undefined values', () => {
        const crbNatureOfInformation: ICrbNatureOfInformation = { id: 123 };
        expectedResult = service.addCrbNatureOfInformationToCollectionIfMissing([], null, crbNatureOfInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbNatureOfInformation);
      });

      it('should return initial array if no CrbNatureOfInformation is added', () => {
        const crbNatureOfInformationCollection: ICrbNatureOfInformation[] = [{ id: 123 }];
        expectedResult = service.addCrbNatureOfInformationToCollectionIfMissing(crbNatureOfInformationCollection, undefined, null);
        expect(expectedResult).toEqual(crbNatureOfInformationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
