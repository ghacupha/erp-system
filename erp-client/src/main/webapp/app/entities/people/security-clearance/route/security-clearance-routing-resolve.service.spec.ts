jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISecurityClearance, SecurityClearance } from '../security-clearance.model';
import { SecurityClearanceService } from '../service/security-clearance.service';

import { SecurityClearanceRoutingResolveService } from './security-clearance-routing-resolve.service';

describe('SecurityClearance routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SecurityClearanceRoutingResolveService;
  let service: SecurityClearanceService;
  let resultSecurityClearance: ISecurityClearance | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SecurityClearanceRoutingResolveService);
    service = TestBed.inject(SecurityClearanceService);
    resultSecurityClearance = undefined;
  });

  describe('resolve', () => {
    it('should return ISecurityClearance returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityClearance = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityClearance).toEqual({ id: 123 });
    });

    it('should return new ISecurityClearance if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityClearance = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSecurityClearance).toEqual(new SecurityClearance());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SecurityClearance })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityClearance = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityClearance).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
