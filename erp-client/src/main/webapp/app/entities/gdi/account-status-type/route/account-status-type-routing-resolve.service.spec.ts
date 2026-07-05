jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAccountStatusType, AccountStatusType } from '../account-status-type.model';
import { AccountStatusTypeService } from '../service/account-status-type.service';

import { AccountStatusTypeRoutingResolveService } from './account-status-type-routing-resolve.service';

describe('AccountStatusType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AccountStatusTypeRoutingResolveService;
  let service: AccountStatusTypeService;
  let resultAccountStatusType: IAccountStatusType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AccountStatusTypeRoutingResolveService);
    service = TestBed.inject(AccountStatusTypeService);
    resultAccountStatusType = undefined;
  });

  describe('resolve', () => {
    it('should return IAccountStatusType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountStatusType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountStatusType).toEqual({ id: 123 });
    });

    it('should return new IAccountStatusType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountStatusType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAccountStatusType).toEqual(new AccountStatusType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountStatusType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountStatusType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountStatusType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
