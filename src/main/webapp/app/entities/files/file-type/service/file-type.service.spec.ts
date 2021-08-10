import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FileMediumTypes } from 'app/entities/enumerations/file-medium-types.model';
import { FileModelType } from 'app/entities/enumerations/file-model-type.model';
import { IFileType, FileType } from '../file-type.model';

import { FileTypeService } from './file-type.service';

describe('Service Tests', () => {
  describe('FileType Service', () => {
    let service: FileTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IFileType;
    let expectedResult: IFileType | IFileType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FileTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fileTypeName: 'AAAAAAA',
        fileMediumType: FileMediumTypes.EXCEL,
        description: 'AAAAAAA',
        fileTemplateContentType: 'image/png',
        fileTemplate: 'AAAAAAA',
        fileType: FileModelType.CURRENCY_LIST,
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
            id: 1,
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

      it('should partial update a FileType', () => {
        const patchObject = Object.assign(
          {
            fileMediumType: 'BBBBBB',
          },
          new FileType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FileType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      describe('addFileTypeToCollectionIfMissing', () => {
        it('should add a FileType to an empty array', () => {
          const fileType: IFileType = { id: 123 };
          expectedResult = service.addFileTypeToCollectionIfMissing([], fileType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fileType);
        });

        it('should not add a FileType to an array that contains it', () => {
          const fileType: IFileType = { id: 123 };
          const fileTypeCollection: IFileType[] = [
            {
              ...fileType,
            },
            { id: 456 },
          ];
          expectedResult = service.addFileTypeToCollectionIfMissing(fileTypeCollection, fileType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FileType to an array that doesn't contain it", () => {
          const fileType: IFileType = { id: 123 };
          const fileTypeCollection: IFileType[] = [{ id: 456 }];
          expectedResult = service.addFileTypeToCollectionIfMissing(fileTypeCollection, fileType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fileType);
        });

        it('should add only unique FileType to an array', () => {
          const fileTypeArray: IFileType[] = [{ id: 123 }, { id: 456 }, { id: 86535 }];
          const fileTypeCollection: IFileType[] = [{ id: 123 }];
          expectedResult = service.addFileTypeToCollectionIfMissing(fileTypeCollection, ...fileTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fileType: IFileType = { id: 123 };
          const fileType2: IFileType = { id: 456 };
          expectedResult = service.addFileTypeToCollectionIfMissing([], fileType, fileType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fileType);
          expect(expectedResult).toContain(fileType2);
        });

        it('should accept null and undefined values', () => {
          const fileType: IFileType = { id: 123 };
          expectedResult = service.addFileTypeToCollectionIfMissing([], null, fileType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fileType);
        });

        it('should return initial array if no FileType is added', () => {
          const fileTypeCollection: IFileType[] = [{ id: 123 }];
          expectedResult = service.addFileTypeToCollectionIfMissing(fileTypeCollection, undefined, null);
          expect(expectedResult).toEqual(fileTypeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
