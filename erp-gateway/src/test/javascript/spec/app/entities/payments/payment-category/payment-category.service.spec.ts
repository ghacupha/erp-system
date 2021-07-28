import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PaymentCategoryService } from 'app/entities/payments/payment-category/payment-category.service';
import { IPaymentCategory, PaymentCategory } from 'app/shared/model/payments/payment-category.model';
import { CategoryTypes } from 'app/shared/model/enumerations/category-types.model';

describe('Service Tests', () => {
  describe('PaymentCategory Service', () => {
    let injector: TestBed;
    let service: PaymentCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentCategory;
    let expectedResult: IPaymentCategory | IPaymentCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PaymentCategoryService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new PaymentCategory(0, 'AAAAAAA', 'AAAAAAA', CategoryTypes.CATEGORY0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PaymentCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PaymentCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentCategory', () => {
        const returnedFromService = Object.assign(
          {
            categoryName: 'BBBBBB',
            categoryDescription: 'BBBBBB',
            categoryType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentCategory', () => {
        const returnedFromService = Object.assign(
          {
            categoryName: 'BBBBBB',
            categoryDescription: 'BBBBBB',
            categoryType: 'BBBBBB',
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

      it('should delete a PaymentCategory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
