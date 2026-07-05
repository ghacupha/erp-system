jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProfessionalQualification, ProfessionalQualification } from '../professional-qualification.model';
import { ProfessionalQualificationService } from '../service/professional-qualification.service';

import { ProfessionalQualificationRoutingResolveService } from './professional-qualification-routing-resolve.service';

describe('ProfessionalQualification routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProfessionalQualificationRoutingResolveService;
  let service: ProfessionalQualificationService;
  let resultProfessionalQualification: IProfessionalQualification | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ProfessionalQualificationRoutingResolveService);
    service = TestBed.inject(ProfessionalQualificationService);
    resultProfessionalQualification = undefined;
  });

  describe('resolve', () => {
    it('should return IProfessionalQualification returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProfessionalQualification = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProfessionalQualification).toEqual({ id: 123 });
    });

    it('should return new IProfessionalQualification if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProfessionalQualification = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProfessionalQualification).toEqual(new ProfessionalQualification());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProfessionalQualification })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProfessionalQualification = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProfessionalQualification).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
