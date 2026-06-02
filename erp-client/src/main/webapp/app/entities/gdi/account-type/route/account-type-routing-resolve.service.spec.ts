jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAccountType, AccountType } from '../account-type.model';
import { AccountTypeService } from '../service/account-type.service';

import { AccountTypeRoutingResolveService } from './account-type-routing-resolve.service';

describe('AccountType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AccountTypeRoutingResolveService;
  let service: AccountTypeService;
  let resultAccountType: IAccountType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AccountTypeRoutingResolveService);
    service = TestBed.inject(AccountTypeService);
    resultAccountType = undefined;
  });

  describe('resolve', () => {
    it('should return IAccountType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountType).toEqual({ id: 123 });
    });

    it('should return new IAccountType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAccountType).toEqual(new AccountType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
