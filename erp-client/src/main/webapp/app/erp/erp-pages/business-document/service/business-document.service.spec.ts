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
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBusinessDocument, BusinessDocument } from '../business-document.model';

import { BusinessDocumentService } from './business-document.service';

describe('BusinessDocument Service', () => {
  let service: BusinessDocumentService;
  let httpMock: HttpTestingController;
  let elemDefault: IBusinessDocument;
  let expectedResult: IBusinessDocument | IBusinessDocument[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusinessDocumentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      documentTitle: 'AAAAAAA',
      description: 'AAAAAAA',
      documentSerial: 'AAAAAAA',
      lastModified: currentDate,
      attachmentFilePath: 'AAAAAAA',
      documentFileContentType: 'image/png',
      documentFile: 'AAAAAAA',
      fileTampered: false,
      documentFileChecksum: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a BusinessDocument', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new BusinessDocument()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusinessDocument', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          documentTitle: 'BBBBBB',
          description: 'BBBBBB',
          documentSerial: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          attachmentFilePath: 'BBBBBB',
          documentFile: 'BBBBBB',
          fileTampered: true,
          documentFileChecksum: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BusinessDocument', () => {
      const patchObject = Object.assign(
        {
          documentTitle: 'BBBBBB',
          documentSerial: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          attachmentFilePath: 'BBBBBB',
          documentFile: 'BBBBBB',
        },
        new BusinessDocument()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusinessDocument', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          documentTitle: 'BBBBBB',
          description: 'BBBBBB',
          documentSerial: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          attachmentFilePath: 'BBBBBB',
          documentFile: 'BBBBBB',
          fileTampered: true,
          documentFileChecksum: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a BusinessDocument', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBusinessDocumentToCollectionIfMissing', () => {
      it('should add a BusinessDocument to an empty array', () => {
        const businessDocument: IBusinessDocument = { id: 123 };
        expectedResult = service.addBusinessDocumentToCollectionIfMissing([], businessDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessDocument);
      });

      it('should not add a BusinessDocument to an array that contains it', () => {
        const businessDocument: IBusinessDocument = { id: 123 };
        const businessDocumentCollection: IBusinessDocument[] = [
          {
            ...businessDocument,
          },
          { id: 456 },
        ];
        expectedResult = service.addBusinessDocumentToCollectionIfMissing(businessDocumentCollection, businessDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusinessDocument to an array that doesn't contain it", () => {
        const businessDocument: IBusinessDocument = { id: 123 };
        const businessDocumentCollection: IBusinessDocument[] = [{ id: 456 }];
        expectedResult = service.addBusinessDocumentToCollectionIfMissing(businessDocumentCollection, businessDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessDocument);
      });

      it('should add only unique BusinessDocument to an array', () => {
        const businessDocumentArray: IBusinessDocument[] = [{ id: 123 }, { id: 456 }, { id: 91443 }];
        const businessDocumentCollection: IBusinessDocument[] = [{ id: 123 }];
        expectedResult = service.addBusinessDocumentToCollectionIfMissing(businessDocumentCollection, ...businessDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const businessDocument: IBusinessDocument = { id: 123 };
        const businessDocument2: IBusinessDocument = { id: 456 };
        expectedResult = service.addBusinessDocumentToCollectionIfMissing([], businessDocument, businessDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessDocument);
        expect(expectedResult).toContain(businessDocument2);
      });

      it('should accept null and undefined values', () => {
        const businessDocument: IBusinessDocument = { id: 123 };
        expectedResult = service.addBusinessDocumentToCollectionIfMissing([], null, businessDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessDocument);
      });

      it('should return initial array if no BusinessDocument is added', () => {
        const businessDocumentCollection: IBusinessDocument[] = [{ id: 123 }];
        expectedResult = service.addBusinessDocumentToCollectionIfMissing(businessDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(businessDocumentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
