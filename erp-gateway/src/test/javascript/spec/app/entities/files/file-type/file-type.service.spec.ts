///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FileTypeService } from 'app/entities/files/file-type/file-type.service';
import { IFileType, FileType } from 'app/shared/model/files/file-type.model';
import { FileMediumTypes } from 'app/shared/model/enumerations/file-medium-types.model';
import { FileModelType } from 'app/shared/model/enumerations/file-model-type.model';

describe('Service Tests', () => {
  describe('FileType Service', () => {
    let injector: TestBed;
    let service: FileTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IFileType;
    let expectedResult: IFileType | IFileType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FileTypeService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new FileType(0, 'AAAAAAA', FileMediumTypes.EXCEL, 'AAAAAAA', 'image/png', 'AAAAAAA', FileModelType.CURRENCY_LIST);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FileType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FileType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FileType', () => {
        const returnedFromService = Object.assign(
          {
            fileTypeName: 'BBBBBB',
            fileMediumType: 'BBBBBB',
            description: 'BBBBBB',
            fileTemplate: 'BBBBBB',
            fileType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FileType', () => {
        const returnedFromService = Object.assign(
          {
            fileTypeName: 'BBBBBB',
            fileMediumType: 'BBBBBB',
            description: 'BBBBBB',
            fileTemplate: 'BBBBBB',
            fileType: 'BBBBBB',
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

      it('should delete a FileType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
