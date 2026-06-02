jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbAgentServiceType, CrbAgentServiceType } from '../crb-agent-service-type.model';
import { CrbAgentServiceTypeService } from '../service/crb-agent-service-type.service';

import { CrbAgentServiceTypeRoutingResolveService } from './crb-agent-service-type-routing-resolve.service';

describe('CrbAgentServiceType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbAgentServiceTypeRoutingResolveService;
  let service: CrbAgentServiceTypeService;
  let resultCrbAgentServiceType: ICrbAgentServiceType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbAgentServiceTypeRoutingResolveService);
    service = TestBed.inject(CrbAgentServiceTypeService);
    resultCrbAgentServiceType = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbAgentServiceType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAgentServiceType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbAgentServiceType).toEqual({ id: 123 });
    });

    it('should return new ICrbAgentServiceType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAgentServiceType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbAgentServiceType).toEqual(new CrbAgentServiceType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbAgentServiceType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAgentServiceType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbAgentServiceType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
