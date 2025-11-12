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
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { User, IUser } from './user.model';

import { UserService } from './user.service';

describe('User Service', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  let expectedResult: IUser | IUser[] | boolean | number | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('Service methods', () => {
    it('should return Users', () => {
      service.query().subscribe(received => {
        expectedResult = received.body;
      });

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([new User(123, 'user')]);
      expect(expectedResult).toEqual([{ id: 123, login: 'user' }]);
    });

    it('should propagate not found response', () => {
      service.query().subscribe({
        error: (error: HttpErrorResponse) => (expectedResult = error.status),
      });

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush('Internal Server Error', {
        status: 500,
        statusText: 'Inernal Server Error',
      });
      expect(expectedResult).toEqual(500);
    });

    describe('addUserToCollectionIfMissing', () => {
      it('should add a User to an empty array', () => {
        const user: IUser = { id: 123 };
        expectedResult = service.addUserToCollectionIfMissing([], user);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(user);
      });

      it('should not add a User to an array that contains it', () => {
        const user: IUser = { id: 123 };
        const userCollection: IUser[] = [
          {
            ...user,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, user);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a User to an array that doesn't contain it", () => {
        const user: IUser = { id: 123 };
        const userCollection: IUser[] = [{ id: 456 }];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, user);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(user);
      });

      it('should add only unique User to an array', () => {
        const userArray: IUser[] = [{ id: 123 }, { id: 456 }, { id: 27699 }];
        const userCollection: IUser[] = [{ id: 456 }];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, ...userArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const user: IUser = { id: 123 };
        const user2: IUser = { id: 456 };
        expectedResult = service.addUserToCollectionIfMissing([], user, user2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(user);
        expect(expectedResult).toContain(user2);
      });

      it('should accept null and undefined values', () => {
        const user: IUser = { id: 123 };
        expectedResult = service.addUserToCollectionIfMissing([], null, user, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(user);
      });

      it('should return initial array if no users is added', () => {
        const userCollection: IUser[] = [{ id: 456 }];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, null, undefined);
        expect(expectedResult).toEqual(userCollection);
      });
    });
  });
});
