jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGenderType, GenderType } from '../gender-type.model';
import { GenderTypeService } from '../service/gender-type.service';

import { GenderTypeRoutingResolveService } from './gender-type-routing-resolve.service';

describe('GenderType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GenderTypeRoutingResolveService;
  let service: GenderTypeService;
  let resultGenderType: IGenderType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(GenderTypeRoutingResolveService);
    service = TestBed.inject(GenderTypeService);
    resultGenderType = undefined;
  });

  describe('resolve', () => {
    it('should return IGenderType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGenderType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGenderType).toEqual({ id: 123 });
    });

    it('should return new IGenderType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGenderType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGenderType).toEqual(new GenderType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GenderType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGenderType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGenderType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
