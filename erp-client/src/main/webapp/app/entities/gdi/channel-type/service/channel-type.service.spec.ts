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

import { IChannelType, ChannelType } from '../channel-type.model';

import { ChannelTypeService } from './channel-type.service';

describe('ChannelType Service', () => {
  let service: ChannelTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IChannelType;
  let expectedResult: IChannelType | IChannelType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChannelTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      channelsTypeCode: 'AAAAAAA',
      channelTypes: 'AAAAAAA',
      channelTypeDetails: 'AAAAAAA',
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

    it('should create a ChannelType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ChannelType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChannelType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          channelsTypeCode: 'BBBBBB',
          channelTypes: 'BBBBBB',
          channelTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChannelType', () => {
      const patchObject = Object.assign(
        {
          channelTypeDetails: 'BBBBBB',
        },
        new ChannelType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChannelType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          channelsTypeCode: 'BBBBBB',
          channelTypes: 'BBBBBB',
          channelTypeDetails: 'BBBBBB',
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

    it('should delete a ChannelType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addChannelTypeToCollectionIfMissing', () => {
      it('should add a ChannelType to an empty array', () => {
        const channelType: IChannelType = { id: 123 };
        expectedResult = service.addChannelTypeToCollectionIfMissing([], channelType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(channelType);
      });

      it('should not add a ChannelType to an array that contains it', () => {
        const channelType: IChannelType = { id: 123 };
        const channelTypeCollection: IChannelType[] = [
          {
            ...channelType,
          },
          { id: 456 },
        ];
        expectedResult = service.addChannelTypeToCollectionIfMissing(channelTypeCollection, channelType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChannelType to an array that doesn't contain it", () => {
        const channelType: IChannelType = { id: 123 };
        const channelTypeCollection: IChannelType[] = [{ id: 456 }];
        expectedResult = service.addChannelTypeToCollectionIfMissing(channelTypeCollection, channelType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(channelType);
      });

      it('should add only unique ChannelType to an array', () => {
        const channelTypeArray: IChannelType[] = [{ id: 123 }, { id: 456 }, { id: 32821 }];
        const channelTypeCollection: IChannelType[] = [{ id: 123 }];
        expectedResult = service.addChannelTypeToCollectionIfMissing(channelTypeCollection, ...channelTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const channelType: IChannelType = { id: 123 };
        const channelType2: IChannelType = { id: 456 };
        expectedResult = service.addChannelTypeToCollectionIfMissing([], channelType, channelType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(channelType);
        expect(expectedResult).toContain(channelType2);
      });

      it('should accept null and undefined values', () => {
        const channelType: IChannelType = { id: 123 };
        expectedResult = service.addChannelTypeToCollectionIfMissing([], null, channelType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(channelType);
      });

      it('should return initial array if no ChannelType is added', () => {
        const channelTypeCollection: IChannelType[] = [{ id: 123 }];
        expectedResult = service.addChannelTypeToCollectionIfMissing(channelTypeCollection, undefined, null);
        expect(expectedResult).toEqual(channelTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
