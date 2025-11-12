///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { ISystemModule, SystemModule } from '../system-module.model';

import { SystemModuleService } from './system-module.service';

describe('SystemModule Service', () => {
  let service: SystemModuleService;
  let httpMock: HttpTestingController;
  let elemDefault: ISystemModule;
  let expectedResult: ISystemModule | ISystemModule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SystemModuleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      moduleName: 'AAAAAAA',
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

    it('should create a SystemModule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SystemModule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemModule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          moduleName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemModule', () => {
      const patchObject = Object.assign(
        {
          moduleName: 'BBBBBB',
        },
        new SystemModule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemModule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          moduleName: 'BBBBBB',
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

    it('should delete a SystemModule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSystemModuleToCollectionIfMissing', () => {
      it('should add a SystemModule to an empty array', () => {
        const systemModule: ISystemModule = { id: 123 };
        expectedResult = service.addSystemModuleToCollectionIfMissing([], systemModule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemModule);
      });

      it('should not add a SystemModule to an array that contains it', () => {
        const systemModule: ISystemModule = { id: 123 };
        const systemModuleCollection: ISystemModule[] = [
          {
            ...systemModule,
          },
          { id: 456 },
        ];
        expectedResult = service.addSystemModuleToCollectionIfMissing(systemModuleCollection, systemModule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemModule to an array that doesn't contain it", () => {
        const systemModule: ISystemModule = { id: 123 };
        const systemModuleCollection: ISystemModule[] = [{ id: 456 }];
        expectedResult = service.addSystemModuleToCollectionIfMissing(systemModuleCollection, systemModule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemModule);
      });

      it('should add only unique SystemModule to an array', () => {
        const systemModuleArray: ISystemModule[] = [{ id: 123 }, { id: 456 }, { id: 76289 }];
        const systemModuleCollection: ISystemModule[] = [{ id: 123 }];
        expectedResult = service.addSystemModuleToCollectionIfMissing(systemModuleCollection, ...systemModuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemModule: ISystemModule = { id: 123 };
        const systemModule2: ISystemModule = { id: 456 };
        expectedResult = service.addSystemModuleToCollectionIfMissing([], systemModule, systemModule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemModule);
        expect(expectedResult).toContain(systemModule2);
      });

      it('should accept null and undefined values', () => {
        const systemModule: ISystemModule = { id: 123 };
        expectedResult = service.addSystemModuleToCollectionIfMissing([], null, systemModule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemModule);
      });

      it('should return initial array if no SystemModule is added', () => {
        const systemModuleCollection: ISystemModule[] = [{ id: 123 }];
        expectedResult = service.addSystemModuleToCollectionIfMissing(systemModuleCollection, undefined, null);
        expect(expectedResult).toEqual(systemModuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
