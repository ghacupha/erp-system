jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISecurityTenure, SecurityTenure } from '../security-tenure.model';
import { SecurityTenureService } from '../service/security-tenure.service';

import { SecurityTenureRoutingResolveService } from './security-tenure-routing-resolve.service';

describe('SecurityTenure routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SecurityTenureRoutingResolveService;
  let service: SecurityTenureService;
  let resultSecurityTenure: ISecurityTenure | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SecurityTenureRoutingResolveService);
    service = TestBed.inject(SecurityTenureService);
    resultSecurityTenure = undefined;
  });

  describe('resolve', () => {
    it('should return ISecurityTenure returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityTenure = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityTenure).toEqual({ id: 123 });
    });

    it('should return new ISecurityTenure if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityTenure = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSecurityTenure).toEqual(new SecurityTenure());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SecurityTenure })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityTenure = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityTenure).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
