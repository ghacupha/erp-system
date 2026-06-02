jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbSourceOfInformationType, CrbSourceOfInformationType } from '../crb-source-of-information-type.model';
import { CrbSourceOfInformationTypeService } from '../service/crb-source-of-information-type.service';

import { CrbSourceOfInformationTypeRoutingResolveService } from './crb-source-of-information-type-routing-resolve.service';

describe('CrbSourceOfInformationType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbSourceOfInformationTypeRoutingResolveService;
  let service: CrbSourceOfInformationTypeService;
  let resultCrbSourceOfInformationType: ICrbSourceOfInformationType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbSourceOfInformationTypeRoutingResolveService);
    service = TestBed.inject(CrbSourceOfInformationTypeService);
    resultCrbSourceOfInformationType = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbSourceOfInformationType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbSourceOfInformationType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbSourceOfInformationType).toEqual({ id: 123 });
    });

    it('should return new ICrbSourceOfInformationType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbSourceOfInformationType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbSourceOfInformationType).toEqual(new CrbSourceOfInformationType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbSourceOfInformationType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbSourceOfInformationType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbSourceOfInformationType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
