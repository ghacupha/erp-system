jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITransactionAccountCategory, TransactionAccountCategory } from '../transaction-account-category.model';
import { TransactionAccountCategoryService } from '../service/transaction-account-category.service';

import { TransactionAccountCategoryRoutingResolveService } from './transaction-account-category-routing-resolve.service';

describe('TransactionAccountCategory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TransactionAccountCategoryRoutingResolveService;
  let service: TransactionAccountCategoryService;
  let resultTransactionAccountCategory: ITransactionAccountCategory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TransactionAccountCategoryRoutingResolveService);
    service = TestBed.inject(TransactionAccountCategoryService);
    resultTransactionAccountCategory = undefined;
  });

  describe('resolve', () => {
    it('should return ITransactionAccountCategory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccountCategory).toEqual({ id: 123 });
    });

    it('should return new ITransactionAccountCategory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountCategory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTransactionAccountCategory).toEqual(new TransactionAccountCategory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TransactionAccountCategory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTransactionAccountCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTransactionAccountCategory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
