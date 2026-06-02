jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITransactionAccount, TransactionAccount } from '../transaction-account.model';
import { TransactionAccountService } from '../service/transaction-account.service';

import { TransactionAccountRoutingResolveService } from './transaction-account-routing-resolve.service';

describe('TransactionAccount routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TransactionAccountRoutingResolveService;
  let service: TransactionAccountService;
  let resultTransactionAccount: ITransactionAccount | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TransactionAccountRoutingResolveService);
    service = TestBed.inject(TransactionAccountService);
    resultTransactionAccount = undefined;
  });

  describe('resolve', () => {
    it('should return ITransactionAccount returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccount = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccount).toEqual({ id: 123 });
    });

    it('should return new ITransactionAccount if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccount = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTransactionAccount).toEqual(new TransactionAccount());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TransactionAccount })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccount = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccount).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
