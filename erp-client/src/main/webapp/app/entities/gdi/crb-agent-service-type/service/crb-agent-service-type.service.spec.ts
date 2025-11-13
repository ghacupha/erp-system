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

import { ICrbAgentServiceType, CrbAgentServiceType } from '../crb-agent-service-type.model';

import { CrbAgentServiceTypeService } from './crb-agent-service-type.service';

describe('CrbAgentServiceType Service', () => {
  let service: CrbAgentServiceTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrbAgentServiceType;
  let expectedResult: ICrbAgentServiceType | ICrbAgentServiceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrbAgentServiceTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      agentServiceTypeCode: 'AAAAAAA',
      agentServiceTypeDetails: 'AAAAAAA',
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

    it('should create a CrbAgentServiceType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CrbAgentServiceType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CrbAgentServiceType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          agentServiceTypeCode: 'BBBBBB',
          agentServiceTypeDetails: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CrbAgentServiceType', () => {
      const patchObject = Object.assign(
        {
          agentServiceTypeDetails: 'BBBBBB',
        },
        new CrbAgentServiceType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CrbAgentServiceType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          agentServiceTypeCode: 'BBBBBB',
          agentServiceTypeDetails: 'BBBBBB',
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

    it('should delete a CrbAgentServiceType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrbAgentServiceTypeToCollectionIfMissing', () => {
      it('should add a CrbAgentServiceType to an empty array', () => {
        const crbAgentServiceType: ICrbAgentServiceType = { id: 123 };
        expectedResult = service.addCrbAgentServiceTypeToCollectionIfMissing([], crbAgentServiceType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAgentServiceType);
      });

      it('should not add a CrbAgentServiceType to an array that contains it', () => {
        const crbAgentServiceType: ICrbAgentServiceType = { id: 123 };
        const crbAgentServiceTypeCollection: ICrbAgentServiceType[] = [
          {
            ...crbAgentServiceType,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrbAgentServiceTypeToCollectionIfMissing(crbAgentServiceTypeCollection, crbAgentServiceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CrbAgentServiceType to an array that doesn't contain it", () => {
        const crbAgentServiceType: ICrbAgentServiceType = { id: 123 };
        const crbAgentServiceTypeCollection: ICrbAgentServiceType[] = [{ id: 456 }];
        expectedResult = service.addCrbAgentServiceTypeToCollectionIfMissing(crbAgentServiceTypeCollection, crbAgentServiceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAgentServiceType);
      });

      it('should add only unique CrbAgentServiceType to an array', () => {
        const crbAgentServiceTypeArray: ICrbAgentServiceType[] = [{ id: 123 }, { id: 456 }, { id: 83280 }];
        const crbAgentServiceTypeCollection: ICrbAgentServiceType[] = [{ id: 123 }];
        expectedResult = service.addCrbAgentServiceTypeToCollectionIfMissing(crbAgentServiceTypeCollection, ...crbAgentServiceTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crbAgentServiceType: ICrbAgentServiceType = { id: 123 };
        const crbAgentServiceType2: ICrbAgentServiceType = { id: 456 };
        expectedResult = service.addCrbAgentServiceTypeToCollectionIfMissing([], crbAgentServiceType, crbAgentServiceType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crbAgentServiceType);
        expect(expectedResult).toContain(crbAgentServiceType2);
      });

      it('should accept null and undefined values', () => {
        const crbAgentServiceType: ICrbAgentServiceType = { id: 123 };
        expectedResult = service.addCrbAgentServiceTypeToCollectionIfMissing([], null, crbAgentServiceType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crbAgentServiceType);
      });

      it('should return initial array if no CrbAgentServiceType is added', () => {
        const crbAgentServiceTypeCollection: ICrbAgentServiceType[] = [{ id: 123 }];
        expectedResult = service.addCrbAgentServiceTypeToCollectionIfMissing(crbAgentServiceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(crbAgentServiceTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
