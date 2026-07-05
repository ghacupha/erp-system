jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILegalStatus, LegalStatus } from '../legal-status.model';
import { LegalStatusService } from '../service/legal-status.service';

import { LegalStatusRoutingResolveService } from './legal-status-routing-resolve.service';

describe('LegalStatus routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LegalStatusRoutingResolveService;
  let service: LegalStatusService;
  let resultLegalStatus: ILegalStatus | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LegalStatusRoutingResolveService);
    service = TestBed.inject(LegalStatusService);
    resultLegalStatus = undefined;
  });

  describe('resolve', () => {
    it('should return ILegalStatus returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLegalStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLegalStatus).toEqual({ id: 123 });
    });

    it('should return new ILegalStatus if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLegalStatus = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLegalStatus).toEqual(new LegalStatus());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LegalStatus })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLegalStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLegalStatus).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
