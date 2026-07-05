jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAgencyNotice, AgencyNotice } from '../agency-notice.model';
import { AgencyNoticeService } from '../service/agency-notice.service';

import { AgencyNoticeRoutingResolveService } from './agency-notice-routing-resolve.service';

describe('AgencyNotice routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AgencyNoticeRoutingResolveService;
  let service: AgencyNoticeService;
  let resultAgencyNotice: IAgencyNotice | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AgencyNoticeRoutingResolveService);
    service = TestBed.inject(AgencyNoticeService);
    resultAgencyNotice = undefined;
  });

  describe('resolve', () => {
    it('should return IAgencyNotice returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAgencyNotice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAgencyNotice).toEqual({ id: 123 });
    });

    it('should return new IAgencyNotice if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAgencyNotice = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAgencyNotice).toEqual(new AgencyNotice());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AgencyNotice })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAgencyNotice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAgencyNotice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
