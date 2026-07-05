import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountOwnershipType } from '../account-ownership-type.model';

import { AccountOwnershipTypeService } from './account-ownership-type.service';

describe('AccountOwnershipType Service', () => {
  let service: AccountOwnershipTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountOwnershipType;
  let expectedResult: IAccountOwnershipType | IAccountOwnershipType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountOwnershipTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      accountOwnershipTypeCode: 'AAAAAAA',
      accountOwnershipType: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should return a list of AccountOwnershipType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          accountOwnershipTypeCode: 'BBBBBB',
          accountOwnershipType: 'BBBBBB',
          description: 'BBBBBB',
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

    describe('addAccountOwnershipTypeToCollectionIfMissing', () => {
      it('should add a AccountOwnershipType to an empty array', () => {
        const accountOwnershipType: IAccountOwnershipType = { id: 123 };
        expectedResult = service.addAccountOwnershipTypeToCollectionIfMissing([], accountOwnershipType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountOwnershipType);
      });

      it('should not add a AccountOwnershipType to an array that contains it', () => {
        const accountOwnershipType: IAccountOwnershipType = { id: 123 };
        const accountOwnershipTypeCollection: IAccountOwnershipType[] = [
          {
            ...accountOwnershipType,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountOwnershipTypeToCollectionIfMissing(accountOwnershipTypeCollection, accountOwnershipType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountOwnershipType to an array that doesn't contain it", () => {
        const accountOwnershipType: IAccountOwnershipType = { id: 123 };
        const accountOwnershipTypeCollection: IAccountOwnershipType[] = [{ id: 456 }];
        expectedResult = service.addAccountOwnershipTypeToCollectionIfMissing(accountOwnershipTypeCollection, accountOwnershipType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountOwnershipType);
      });

      it('should add only unique AccountOwnershipType to an array', () => {
        const accountOwnershipTypeArray: IAccountOwnershipType[] = [{ id: 123 }, { id: 456 }, { id: 62388 }];
        const accountOwnershipTypeCollection: IAccountOwnershipType[] = [{ id: 123 }];
        expectedResult = service.addAccountOwnershipTypeToCollectionIfMissing(accountOwnershipTypeCollection, ...accountOwnershipTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountOwnershipType: IAccountOwnershipType = { id: 123 };
        const accountOwnershipType2: IAccountOwnershipType = { id: 456 };
        expectedResult = service.addAccountOwnershipTypeToCollectionIfMissing([], accountOwnershipType, accountOwnershipType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountOwnershipType);
        expect(expectedResult).toContain(accountOwnershipType2);
      });

      it('should accept null and undefined values', () => {
        const accountOwnershipType: IAccountOwnershipType = { id: 123 };
        expectedResult = service.addAccountOwnershipTypeToCollectionIfMissing([], null, accountOwnershipType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountOwnershipType);
      });

      it('should return initial array if no AccountOwnershipType is added', () => {
        const accountOwnershipTypeCollection: IAccountOwnershipType[] = [{ id: 123 }];
        expectedResult = service.addAccountOwnershipTypeToCollectionIfMissing(accountOwnershipTypeCollection, undefined, null);
        expect(expectedResult).toEqual(accountOwnershipTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
