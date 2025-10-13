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

import { IWorkProjectRegister, WorkProjectRegister } from '../work-project-register.model';

import { WorkProjectRegisterService } from './work-project-register.service';

describe('WorkProjectRegister Service', () => {
  let service: WorkProjectRegisterService;
  let httpMock: HttpTestingController;
  let elemDefault: IWorkProjectRegister;
  let expectedResult: IWorkProjectRegister | IWorkProjectRegister[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkProjectRegisterService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      catalogueNumber: 'AAAAAAA',
      projectTitle: 'AAAAAAA',
      description: 'AAAAAAA',
      detailsContentType: 'image/png',
      details: 'AAAAAAA',
      totalProjectCost: 0,
      additionalNotesContentType: 'image/png',
      additionalNotes: 'AAAAAAA',
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

    it('should create a WorkProjectRegister', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new WorkProjectRegister()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkProjectRegister', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          projectTitle: 'BBBBBB',
          description: 'BBBBBB',
          details: 'BBBBBB',
          totalProjectCost: 1,
          additionalNotes: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkProjectRegister', () => {
      const patchObject = Object.assign(
        {
          catalogueNumber: 'BBBBBB',
          projectTitle: 'BBBBBB',
          details: 'BBBBBB',
        },
        new WorkProjectRegister()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkProjectRegister', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          catalogueNumber: 'BBBBBB',
          projectTitle: 'BBBBBB',
          description: 'BBBBBB',
          details: 'BBBBBB',
          totalProjectCost: 1,
          additionalNotes: 'BBBBBB',
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

    it('should delete a WorkProjectRegister', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWorkProjectRegisterToCollectionIfMissing', () => {
      it('should add a WorkProjectRegister to an empty array', () => {
        const workProjectRegister: IWorkProjectRegister = { id: 123 };
        expectedResult = service.addWorkProjectRegisterToCollectionIfMissing([], workProjectRegister);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workProjectRegister);
      });

      it('should not add a WorkProjectRegister to an array that contains it', () => {
        const workProjectRegister: IWorkProjectRegister = { id: 123 };
        const workProjectRegisterCollection: IWorkProjectRegister[] = [
          {
            ...workProjectRegister,
          },
          { id: 456 },
        ];
        expectedResult = service.addWorkProjectRegisterToCollectionIfMissing(workProjectRegisterCollection, workProjectRegister);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkProjectRegister to an array that doesn't contain it", () => {
        const workProjectRegister: IWorkProjectRegister = { id: 123 };
        const workProjectRegisterCollection: IWorkProjectRegister[] = [{ id: 456 }];
        expectedResult = service.addWorkProjectRegisterToCollectionIfMissing(workProjectRegisterCollection, workProjectRegister);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workProjectRegister);
      });

      it('should add only unique WorkProjectRegister to an array', () => {
        const workProjectRegisterArray: IWorkProjectRegister[] = [{ id: 123 }, { id: 456 }, { id: 4720 }];
        const workProjectRegisterCollection: IWorkProjectRegister[] = [{ id: 123 }];
        expectedResult = service.addWorkProjectRegisterToCollectionIfMissing(workProjectRegisterCollection, ...workProjectRegisterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workProjectRegister: IWorkProjectRegister = { id: 123 };
        const workProjectRegister2: IWorkProjectRegister = { id: 456 };
        expectedResult = service.addWorkProjectRegisterToCollectionIfMissing([], workProjectRegister, workProjectRegister2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workProjectRegister);
        expect(expectedResult).toContain(workProjectRegister2);
      });

      it('should accept null and undefined values', () => {
        const workProjectRegister: IWorkProjectRegister = { id: 123 };
        expectedResult = service.addWorkProjectRegisterToCollectionIfMissing([], null, workProjectRegister, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workProjectRegister);
      });

      it('should return initial array if no WorkProjectRegister is added', () => {
        const workProjectRegisterCollection: IWorkProjectRegister[] = [{ id: 123 }];
        expectedResult = service.addWorkProjectRegisterToCollectionIfMissing(workProjectRegisterCollection, undefined, null);
        expect(expectedResult).toEqual(workProjectRegisterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
