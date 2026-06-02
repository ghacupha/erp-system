jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAccountOwnershipType, AccountOwnershipType } from '../account-ownership-type.model';
import { AccountOwnershipTypeService } from '../service/account-ownership-type.service';

import { AccountOwnershipTypeRoutingResolveService } from './account-ownership-type-routing-resolve.service';

describe('AccountOwnershipType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AccountOwnershipTypeRoutingResolveService;
  let service: AccountOwnershipTypeService;
  let resultAccountOwnershipType: IAccountOwnershipType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AccountOwnershipTypeRoutingResolveService);
    service = TestBed.inject(AccountOwnershipTypeService);
    resultAccountOwnershipType = undefined;
  });

  describe('resolve', () => {
    it('should return IAccountOwnershipType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountOwnershipType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountOwnershipType).toEqual({ id: 123 });
    });

    it('should return new IAccountOwnershipType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountOwnershipType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAccountOwnershipType).toEqual(new AccountOwnershipType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountOwnershipType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountOwnershipType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountOwnershipType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
