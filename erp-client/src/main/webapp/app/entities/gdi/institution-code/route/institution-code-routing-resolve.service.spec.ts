jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInstitutionCode, InstitutionCode } from '../institution-code.model';
import { InstitutionCodeService } from '../service/institution-code.service';

import { InstitutionCodeRoutingResolveService } from './institution-code-routing-resolve.service';

describe('InstitutionCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InstitutionCodeRoutingResolveService;
  let service: InstitutionCodeService;
  let resultInstitutionCode: IInstitutionCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InstitutionCodeRoutingResolveService);
    service = TestBed.inject(InstitutionCodeService);
    resultInstitutionCode = undefined;
  });

  describe('resolve', () => {
    it('should return IInstitutionCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitutionCode).toEqual({ id: 123 });
    });

    it('should return new IInstitutionCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInstitutionCode).toEqual(new InstitutionCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InstitutionCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitutionCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
