jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISecurityType, SecurityType } from '../security-type.model';
import { SecurityTypeService } from '../service/security-type.service';

import { SecurityTypeRoutingResolveService } from './security-type-routing-resolve.service';

describe('SecurityType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SecurityTypeRoutingResolveService;
  let service: SecurityTypeService;
  let resultSecurityType: ISecurityType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SecurityTypeRoutingResolveService);
    service = TestBed.inject(SecurityTypeService);
    resultSecurityType = undefined;
  });

  describe('resolve', () => {
    it('should return ISecurityType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityType).toEqual({ id: 123 });
    });

    it('should return new ISecurityType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSecurityType).toEqual(new SecurityType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SecurityType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
