jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBankTransactionType, BankTransactionType } from '../bank-transaction-type.model';
import { BankTransactionTypeService } from '../service/bank-transaction-type.service';

import { BankTransactionTypeRoutingResolveService } from './bank-transaction-type-routing-resolve.service';

describe('BankTransactionType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BankTransactionTypeRoutingResolveService;
  let service: BankTransactionTypeService;
  let resultBankTransactionType: IBankTransactionType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BankTransactionTypeRoutingResolveService);
    service = TestBed.inject(BankTransactionTypeService);
    resultBankTransactionType = undefined;
  });

  describe('resolve', () => {
    it('should return IBankTransactionType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankTransactionType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankTransactionType).toEqual({ id: 123 });
    });

    it('should return new IBankTransactionType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankTransactionType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBankTransactionType).toEqual(new BankTransactionType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BankTransactionType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankTransactionType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankTransactionType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
