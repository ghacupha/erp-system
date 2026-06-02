jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInstitutionContactDetails, InstitutionContactDetails } from '../institution-contact-details.model';
import { InstitutionContactDetailsService } from '../service/institution-contact-details.service';

import { InstitutionContactDetailsRoutingResolveService } from './institution-contact-details-routing-resolve.service';

describe('InstitutionContactDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InstitutionContactDetailsRoutingResolveService;
  let service: InstitutionContactDetailsService;
  let resultInstitutionContactDetails: IInstitutionContactDetails | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InstitutionContactDetailsRoutingResolveService);
    service = TestBed.inject(InstitutionContactDetailsService);
    resultInstitutionContactDetails = undefined;
  });

  describe('resolve', () => {
    it('should return IInstitutionContactDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionContactDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitutionContactDetails).toEqual({ id: 123 });
    });

    it('should return new IInstitutionContactDetails if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionContactDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInstitutionContactDetails).toEqual(new InstitutionContactDetails());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InstitutionContactDetails })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionContactDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitutionContactDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
