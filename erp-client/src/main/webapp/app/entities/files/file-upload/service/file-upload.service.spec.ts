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

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFileUpload, FileUpload } from '../file-upload.model';

import { FileUploadService } from './file-upload.service';

describe('FileUpload Service', () => {
  let service: FileUploadService;
  let httpMock: HttpTestingController;
  let elemDefault: IFileUpload;
  let expectedResult: IFileUpload | IFileUpload[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FileUploadService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      fileName: 'AAAAAAA',
      periodFrom: currentDate,
      periodTo: currentDate,
      fileTypeId: 0,
      dataFileContentType: 'image/png',
      dataFile: 'AAAAAAA',
      uploadSuccessful: false,
      uploadProcessed: false,
      uploadToken: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          periodFrom: currentDate.format(DATE_FORMAT),
          periodTo: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FileUpload', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          periodFrom: currentDate.format(DATE_FORMAT),
          periodTo: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          periodFrom: currentDate,
          periodTo: currentDate,
        },
        returnedFromService
      );

      service.create(new FileUpload()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileUpload', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          fileName: 'BBBBBB',
          periodFrom: currentDate.format(DATE_FORMAT),
          periodTo: currentDate.format(DATE_FORMAT),
          fileTypeId: 1,
          dataFile: 'BBBBBB',
          uploadSuccessful: true,
          uploadProcessed: true,
          uploadToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          periodFrom: currentDate,
          periodTo: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FileUpload', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          uploadSuccessful: true,
          uploadProcessed: true,
        },
        new FileUpload()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          periodFrom: currentDate,
          periodTo: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FileUpload', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          fileName: 'BBBBBB',
          periodFrom: currentDate.format(DATE_FORMAT),
          periodTo: currentDate.format(DATE_FORMAT),
          fileTypeId: 1,
          dataFile: 'BBBBBB',
          uploadSuccessful: true,
          uploadProcessed: true,
          uploadToken: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          periodFrom: currentDate,
          periodTo: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FileUpload', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFileUploadToCollectionIfMissing', () => {
      it('should add a FileUpload to an empty array', () => {
        const fileUpload: IFileUpload = { id: 123 };
        expectedResult = service.addFileUploadToCollectionIfMissing([], fileUpload);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileUpload);
      });

      it('should not add a FileUpload to an array that contains it', () => {
        const fileUpload: IFileUpload = { id: 123 };
        const fileUploadCollection: IFileUpload[] = [
          {
            ...fileUpload,
          },
          { id: 456 },
        ];
        expectedResult = service.addFileUploadToCollectionIfMissing(fileUploadCollection, fileUpload);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileUpload to an array that doesn't contain it", () => {
        const fileUpload: IFileUpload = { id: 123 };
        const fileUploadCollection: IFileUpload[] = [{ id: 456 }];
        expectedResult = service.addFileUploadToCollectionIfMissing(fileUploadCollection, fileUpload);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileUpload);
      });

      it('should add only unique FileUpload to an array', () => {
        const fileUploadArray: IFileUpload[] = [{ id: 123 }, { id: 456 }, { id: 26127 }];
        const fileUploadCollection: IFileUpload[] = [{ id: 123 }];
        expectedResult = service.addFileUploadToCollectionIfMissing(fileUploadCollection, ...fileUploadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileUpload: IFileUpload = { id: 123 };
        const fileUpload2: IFileUpload = { id: 456 };
        expectedResult = service.addFileUploadToCollectionIfMissing([], fileUpload, fileUpload2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileUpload);
        expect(expectedResult).toContain(fileUpload2);
      });

      it('should accept null and undefined values', () => {
        const fileUpload: IFileUpload = { id: 123 };
        expectedResult = service.addFileUploadToCollectionIfMissing([], null, fileUpload, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileUpload);
      });

      it('should return initial array if no FileUpload is added', () => {
        const fileUploadCollection: IFileUpload[] = [{ id: 123 }];
        expectedResult = service.addFileUploadToCollectionIfMissing(fileUploadCollection, undefined, null);
        expect(expectedResult).toEqual(fileUploadCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
