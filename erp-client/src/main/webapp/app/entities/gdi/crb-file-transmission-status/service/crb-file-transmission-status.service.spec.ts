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

import { SubmittedFileStatusTypes } from 'app/entities/enumerations/submitted-file-status-types.model';
import { ICrbFileTransmissionStatus, CrbFileTransmissionStatus } from '../crb-file-transmission-status.model';

import { CrbFileTransmissionStatusService } from './crb-file-transmission-status.service';

describe('CrbFileTransmissionStatus Service', () => {
  let service: CrbFileTransmissionStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbFileTransmissionStatus;
  let expectedResult: ICrbFileTransmissionStatus | ICrbFileTransmissionStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbFileTransmissionStatusService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      submittedFileStatusTypeCode: 'AAAAAAA',
      submittedFileStatusType: SubmittedFileStatusTypes.CORRECT,
      submittedFileStatusTypeDescription: 'AAAAAAA',
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

    it('should create a CrbFileTransmissionStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbFileTransmissionStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbFileTransmissionStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          submittedFileStatusTypeCode: 'BBBBBB',
          submittedFileStatusType: 'BBBBBB',
          submittedFileStatusTypeDescription: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbFileTransmissionStatus', () => {
      const patchObject = Object.assign(
        {
          submittedFileStatusType: 'BBBBBB',
          submittedFileStatusTypeDescription: 'BBBBBB',
        },
        new CrbFileTransmissionStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbFileTransmissionStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          submittedFileStatusTypeCode: 'BBBBBB',
          submittedFileStatusType: 'BBBBBB',
          submittedFileStatusTypeDescription: 'BBBBBB',
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

    it('should delete a CrbFileTransmissionStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbFileTransmissionStatusToCollectionIfMissing', () => {
      it('should add a CrbFileTransmissionStatus to an empty array', () => {
        const crbFileTransmissionStatus: ICrbFileTransmissionStatus = { id: 123 };
        expectedResult = service.addCrbFileTransmissionStatusToCollectionIfMissing([], crbFileTransmissionStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbFileTransmissionStatus);
      });

      it('should not add a CrbFileTransmissionStatus to an array that contains it', () => {
        const crbFileTransmissionStatus: ICrbFileTransmissionStatus = { id: 123 };
        const crbFileTransmissionStatusCollection: ICrbFileTransmissionStatus[] = [
          {
            ...crbFileTransmissionStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbFileTransmissionStatusToCollectionIfMissing(
          crbFileTransmissionStatusCollection,
          crbFileTransmissionStatus
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbFileTransmissionStatus to an array that doesn't contain it", () => {
        const crbFileTransmissionStatus: ICrbFileTransmissionStatus = { id: 123 };
        const crbFileTransmissionStatusCollection: ICrbFileTransmissionStatus[] = [{ id: 456 }];
        expectedResult = service.addCrbFileTransmissionStatusToCollectionIfMissing(
          crbFileTransmissionStatusCollection,
          crbFileTransmissionStatus
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbFileTransmissionStatus);
      });

      it('should add only unique CrbFileTransmissionStatus to an array', () => {
        const crbFileTransmissionStatusArray: ICrbFileTransmissionStatus[] = [{ id: 123 }, { id: 456 }, { id: 61854 }];
        const crbFileTransmissionStatusCollection: ICrbFileTransmissionStatus[] = [{ id: 123 }];
        expectedResult = service.addCrbFileTransmissionStatusToCollectionIfMissing(
          crbFileTransmissionStatusCollection,
          ...crbFileTransmissionStatusArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbFileTransmissionStatus: ICrbFileTransmissionStatus = { id: 123 };
        const crbFileTransmissionStatus2: ICrbFileTransmissionStatus = { id: 456 };
        expectedResult = service.addCrbFileTransmissionStatusToCollectionIfMissing(
          [],
          crbFileTransmissionStatus,
          crbFileTransmissionStatus2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbFileTransmissionStatus);
        expect(expectedResult).toContain(crbFileTransmissionStatus2);
      });

      it('should accept null and undefined values', () => {
        const crbFileTransmissionStatus: ICrbFileTransmissionStatus = { id: 123 };
        expectedResult = service.addCrbFileTransmissionStatusToCollectionIfMissing([], null, crbFileTransmissionStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbFileTransmissionStatus);
      });

      it('should return initial array if no CrbFileTransmissionStatus is added', () => {
        const crbFileTransmissionStatusCollection: ICrbFileTransmissionStatus[] = [{ id: 123 }];
        expectedResult = service.addCrbFileTransmissionStatusToCollectionIfMissing(crbFileTransmissionStatusCollection, undefined, null);
        expect(expectedResult).toEqual(crbFileTransmissionStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
