jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import {
  ITransactionAccountPostingProcessType,
  TransactionAccountPostingProcessType,
} from '../transaction-account-posting-process-type.model';
import { TransactionAccountPostingProcessTypeService } from '../service/transaction-account-posting-process-type.service';

import { TransactionAccountPostingProcessTypeRoutingResolveService } from './transaction-account-posting-process-type-routing-resolve.service';

describe('TransactionAccountPostingProcessType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TransactionAccountPostingProcessTypeRoutingResolveService;
  let service: TransactionAccountPostingProcessTypeService;
  let resultTransactionAccountPostingProcessType: ITransactionAccountPostingProcessType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TransactionAccountPostingProcessTypeRoutingResolveService);
    service = TestBed.inject(TransactionAccountPostingProcessTypeService);
    resultTransactionAccountPostingProcessType = undefined;
  });

  describe('resolve', () => {
    it('should return ITransactionAccountPostingProcessType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountPostingProcessType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccountPostingProcessType).toEqual({ id: 123 });
    });

    it('should return new ITransactionAccountPostingProcessType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountPostingProcessType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTransactionAccountPostingProcessType).toEqual(new TransactionAccountPostingProcessType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TransactionAccountPostingProcessType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountPostingProcessType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccountPostingProcessType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
