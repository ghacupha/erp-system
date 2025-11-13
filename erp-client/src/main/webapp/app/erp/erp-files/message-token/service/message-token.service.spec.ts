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

import { IMessageToken, MessageToken } from '../message-token.model';

import { MessageTokenService } from './message-token.service';

describe('Service Tests', () => {
  describe('MessageToken Service', () => {
    let service: MessageTokenService;
    let httpMock: HttpTestingController;
    let elemDefault: IMessageToken;
    let expectedResult: IMessageToken | IMessageToken[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MessageTokenService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        description: 'AAAAAAA',
        timeSent: 0,
        tokenValue: 'AAAAAAA',
        received: false,
        actioned: false,
        contentFullyEnqueued: false,
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

      it('should create a MessageToken', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MessageToken()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MessageToken', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            description: 'BBBBBB',
            timeSent: 1,
            tokenValue: 'BBBBBB',
            received: true,
            actioned: true,
            contentFullyEnqueued: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MessageToken', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            tokenValue: 'BBBBBB',
            actioned: true,
            contentFullyEnqueued: true,
          },
          new MessageToken()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MessageToken', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            description: 'BBBBBB',
            timeSent: 1,
            tokenValue: 'BBBBBB',
            received: true,
            actioned: true,
            contentFullyEnqueued: true,
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

      it('should delete a MessageToken', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMessageTokenToCollectionIfMissing', () => {
        it('should add a MessageToken to an empty array', () => {
          const messageToken: IMessageToken = { id: 123 };
          expectedResult = service.addMessageTokenToCollectionIfMissing([], messageToken);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(messageToken);
        });

        it('should not add a MessageToken to an array that contains it', () => {
          const messageToken: IMessageToken = { id: 123 };
          const messageTokenCollection: IMessageToken[] = [
            {
              ...messageToken,
            },
            { id: 456 },
          ];
          expectedResult = service.addMessageTokenToCollectionIfMissing(messageTokenCollection, messageToken);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MessageToken to an array that doesn't contain it", () => {
          const messageToken: IMessageToken = { id: 123 };
          const messageTokenCollection: IMessageToken[] = [{ id: 456 }];
          expectedResult = service.addMessageTokenToCollectionIfMissing(messageTokenCollection, messageToken);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(messageToken);
        });

        it('should add only unique MessageToken to an array', () => {
          const messageTokenArray: IMessageToken[] = [{ id: 123 }, { id: 456 }, { id: 64537 }];
          const messageTokenCollection: IMessageToken[] = [{ id: 123 }];
          expectedResult = service.addMessageTokenToCollectionIfMissing(messageTokenCollection, ...messageTokenArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const messageToken: IMessageToken = { id: 123 };
          const messageToken2: IMessageToken = { id: 456 };
          expectedResult = service.addMessageTokenToCollectionIfMissing([], messageToken, messageToken2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(messageToken);
          expect(expectedResult).toContain(messageToken2);
        });

        it('should accept null and undefined values', () => {
          const messageToken: IMessageToken = { id: 123 };
          expectedResult = service.addMessageTokenToCollectionIfMissing([], null, messageToken, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(messageToken);
        });

        it('should return initial array if no MessageToken is added', () => {
          const messageTokenCollection: IMessageToken[] = [{ id: 123 }];
          expectedResult = service.addMessageTokenToCollectionIfMissing(messageTokenCollection, undefined, null);
          expect(expectedResult).toEqual(messageTokenCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
