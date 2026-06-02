jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAccountAttribute, AccountAttribute } from '../account-attribute.model';
import { AccountAttributeService } from '../service/account-attribute.service';

import { AccountAttributeRoutingResolveService } from './account-attribute-routing-resolve.service';

describe('AccountAttribute routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AccountAttributeRoutingResolveService;
  let service: AccountAttributeService;
  let resultAccountAttribute: IAccountAttribute | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AccountAttributeRoutingResolveService);
    service = TestBed.inject(AccountAttributeService);
    resultAccountAttribute = undefined;
  });

  describe('resolve', () => {
    it('should return IAccountAttribute returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountAttribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountAttribute).toEqual({ id: 123 });
    });

    it('should return new IAccountAttribute if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountAttribute = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAccountAttribute).toEqual(new AccountAttribute());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountAttribute })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountAttribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountAttribute).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
