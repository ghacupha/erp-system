import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeaseTemplate, LeaseTemplate } from '../lease-template.model';

import { LeaseTemplateService } from './lease-template.service';

describe('LeaseTemplate Service', () => {
  let service: LeaseTemplateService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeaseTemplate;
  let expectedResult: ILeaseTemplate | ILeaseTemplate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaseTemplateService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      templateTitle: 'AAAAAAA',
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

    it('should create a LeaseTemplate', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LeaseTemplate()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LeaseTemplate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          templateTitle: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LeaseTemplate', () => {
      const patchObject = Object.assign(
        {
          templateTitle: 'BBBBBB',
        },
        new LeaseTemplate()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LeaseTemplate', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          templateTitle: 'BBBBBB',
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

    it('should delete a LeaseTemplate', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaseTemplateToCollectionIfMissing', () => {
      it('should add a LeaseTemplate to an empty array', () => {
        const leaseTemplate: ILeaseTemplate = { id: 123 };
        expectedResult = service.addLeaseTemplateToCollectionIfMissing([], leaseTemplate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseTemplate);
      });

      it('should not add a LeaseTemplate to an array that contains it', () => {
        const leaseTemplate: ILeaseTemplate = { id: 123 };
        const leaseTemplateCollection: ILeaseTemplate[] = [
          {
            ...leaseTemplate,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaseTemplateToCollectionIfMissing(leaseTemplateCollection, leaseTemplate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LeaseTemplate to an array that doesn't contain it", () => {
        const leaseTemplate: ILeaseTemplate = { id: 123 };
        const leaseTemplateCollection: ILeaseTemplate[] = [{ id: 456 }];
        expectedResult = service.addLeaseTemplateToCollectionIfMissing(leaseTemplateCollection, leaseTemplate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseTemplate);
      });

      it('should add only unique LeaseTemplate to an array', () => {
        const leaseTemplateArray: ILeaseTemplate[] = [{ id: 123 }, { id: 456 }, { id: 52315 }];
        const leaseTemplateCollection: ILeaseTemplate[] = [{ id: 123 }];
        expectedResult = service.addLeaseTemplateToCollectionIfMissing(leaseTemplateCollection, ...leaseTemplateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leaseTemplate: ILeaseTemplate = { id: 123 };
        const leaseTemplate2: ILeaseTemplate = { id: 456 };
        expectedResult = service.addLeaseTemplateToCollectionIfMissing([], leaseTemplate, leaseTemplate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leaseTemplate);
        expect(expectedResult).toContain(leaseTemplate2);
      });

      it('should accept null and undefined values', () => {
        const leaseTemplate: ILeaseTemplate = { id: 123 };
        expectedResult = service.addLeaseTemplateToCollectionIfMissing([], null, leaseTemplate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leaseTemplate);
      });

      it('should return initial array if no LeaseTemplate is added', () => {
        const leaseTemplateCollection: ILeaseTemplate[] = [{ id: 123 }];
        expectedResult = service.addLeaseTemplateToCollectionIfMissing(leaseTemplateCollection, undefined, null);
        expect(expectedResult).toEqual(leaseTemplateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
