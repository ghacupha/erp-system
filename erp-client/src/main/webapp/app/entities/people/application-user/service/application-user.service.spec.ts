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

import { IApplicationUser, ApplicationUser } from '../application-user.model';

import { ApplicationUserService } from './application-user.service';

describe('ApplicationUser Service', () => {
  let service: ApplicationUserService;
  let httpMock: HttpTestingController;
  let elemDefault: IApplicationUser;
  let expectedResult: IApplicationUser | IApplicationUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApplicationUserService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      designation: 'AAAAAAA',
      applicationIdentity: 'AAAAAAA',
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

    it('should create a ApplicationUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ApplicationUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApplicationUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          designation: 'BBBBBB',
          applicationIdentity: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApplicationUser', () => {
      const patchObject = Object.assign(
        {
          designation: 'BBBBBB',
          applicationIdentity: 'BBBBBB',
        },
        new ApplicationUser()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApplicationUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          designation: 'BBBBBB',
          applicationIdentity: 'BBBBBB',
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

    it('should delete a ApplicationUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApplicationUserToCollectionIfMissing', () => {
      it('should add a ApplicationUser to an empty array', () => {
        const applicationUser: IApplicationUser = { id: 123 };
        expectedResult = service.addApplicationUserToCollectionIfMissing([], applicationUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicationUser);
      });

      it('should not add a ApplicationUser to an array that contains it', () => {
        const applicationUser: IApplicationUser = { id: 123 };
        const applicationUserCollection: IApplicationUser[] = [
          {
            ...applicationUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addApplicationUserToCollectionIfMissing(applicationUserCollection, applicationUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApplicationUser to an array that doesn't contain it", () => {
        const applicationUser: IApplicationUser = { id: 123 };
        const applicationUserCollection: IApplicationUser[] = [{ id: 456 }];
        expectedResult = service.addApplicationUserToCollectionIfMissing(applicationUserCollection, applicationUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicationUser);
      });

      it('should add only unique ApplicationUser to an array', () => {
        const applicationUserArray: IApplicationUser[] = [{ id: 123 }, { id: 456 }, { id: 37066 }];
        const applicationUserCollection: IApplicationUser[] = [{ id: 123 }];
        expectedResult = service.addApplicationUserToCollectionIfMissing(applicationUserCollection, ...applicationUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const applicationUser: IApplicationUser = { id: 123 };
        const applicationUser2: IApplicationUser = { id: 456 };
        expectedResult = service.addApplicationUserToCollectionIfMissing([], applicationUser, applicationUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicationUser);
        expect(expectedResult).toContain(applicationUser2);
      });

      it('should accept null and undefined values', () => {
        const applicationUser: IApplicationUser = { id: 123 };
        expectedResult = service.addApplicationUserToCollectionIfMissing([], null, applicationUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicationUser);
      });

      it('should return initial array if no ApplicationUser is added', () => {
        const applicationUserCollection: IApplicationUser[] = [{ id: 123 }];
        expectedResult = service.addApplicationUserToCollectionIfMissing(applicationUserCollection, undefined, null);
        expect(expectedResult).toEqual(applicationUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
