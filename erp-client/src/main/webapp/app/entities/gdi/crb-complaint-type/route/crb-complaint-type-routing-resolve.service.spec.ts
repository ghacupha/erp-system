jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbComplaintType, CrbComplaintType } from '../crb-complaint-type.model';
import { CrbComplaintTypeService } from '../service/crb-complaint-type.service';

import { CrbComplaintTypeRoutingResolveService } from './crb-complaint-type-routing-resolve.service';

describe('CrbComplaintType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbComplaintTypeRoutingResolveService;
  let service: CrbComplaintTypeService;
  let resultCrbComplaintType: ICrbComplaintType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbComplaintTypeRoutingResolveService);
    service = TestBed.inject(CrbComplaintTypeService);
    resultCrbComplaintType = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbComplaintType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbComplaintType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbComplaintType).toEqual({ id: 123 });
    });

    it('should return new ICrbComplaintType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbComplaintType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbComplaintType).toEqual(new CrbComplaintType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbComplaintType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbComplaintType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbComplaintType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
