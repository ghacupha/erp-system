jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEmploymentTerms, EmploymentTerms } from '../employment-terms.model';
import { EmploymentTermsService } from '../service/employment-terms.service';

import { EmploymentTermsRoutingResolveService } from './employment-terms-routing-resolve.service';

describe('EmploymentTerms routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EmploymentTermsRoutingResolveService;
  let service: EmploymentTermsService;
  let resultEmploymentTerms: IEmploymentTerms | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(EmploymentTermsRoutingResolveService);
    service = TestBed.inject(EmploymentTermsService);
    resultEmploymentTerms = undefined;
  });

  describe('resolve', () => {
    it('should return IEmploymentTerms returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmploymentTerms = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmploymentTerms).toEqual({ id: 123 });
    });

    it('should return new IEmploymentTerms if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmploymentTerms = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEmploymentTerms).toEqual(new EmploymentTerms());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EmploymentTerms })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEmploymentTerms = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEmploymentTerms).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
