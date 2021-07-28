import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { TaxRuleService } from 'app/entities/payments/tax-rule/tax-rule.service';
import { ITaxRule, TaxRule } from 'app/shared/model/payments/tax-rule.model';

describe('Service Tests', () => {
  describe('TaxRule Service', () => {
    let injector: TestBed;
    let service: TaxRuleService;
    let httpMock: HttpTestingController;
    let elemDefault: ITaxRule;
    let expectedResult: ITaxRule | ITaxRule[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TaxRuleService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new TaxRule(0, 'AAAAAAA', currentDate, 0, 0, 0, 0, 0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            paymentDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TaxRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            paymentDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            paymentDate: currentDate,
          },
          returnedFromService
        );

        service.create(new TaxRule()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TaxRule', () => {
        const returnedFromService = Object.assign(
          {
            paymentNumber: 'BBBBBB',
            paymentDate: currentDate.format(DATE_FORMAT),
            telcoExciseDuty: 1,
            valueAddedTax: 1,
            withholdingVAT: 1,
            withholdingTaxConsultancy: 1,
            withholdingTaxRent: 1,
            cateringLevy: 1,
            serviceCharge: 1,
            withholdingTaxImportedService: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            paymentDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TaxRule', () => {
        const returnedFromService = Object.assign(
          {
            paymentNumber: 'BBBBBB',
            paymentDate: currentDate.format(DATE_FORMAT),
            telcoExciseDuty: 1,
            valueAddedTax: 1,
            withholdingVAT: 1,
            withholdingTaxConsultancy: 1,
            withholdingTaxRent: 1,
            cateringLevy: 1,
            serviceCharge: 1,
            withholdingTaxImportedService: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            paymentDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TaxRule', () => {
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
