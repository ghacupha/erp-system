jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseTemplate, LeaseTemplate } from '../lease-template.model';
import { LeaseTemplateService } from '../service/lease-template.service';

import { LeaseTemplateRoutingResolveService } from './lease-template-routing-resolve.service';

describe('LeaseTemplate routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseTemplateRoutingResolveService;
  let service: LeaseTemplateService;
  let resultLeaseTemplate: ILeaseTemplate | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseTemplateRoutingResolveService);
    service = TestBed.inject(LeaseTemplateService);
    resultLeaseTemplate = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseTemplate returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseTemplate = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseTemplate).toEqual({ id: 123 });
    });

    it('should return new ILeaseTemplate if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseTemplate = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseTemplate).toEqual(new LeaseTemplate());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseTemplate })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseTemplate = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseTemplate).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
