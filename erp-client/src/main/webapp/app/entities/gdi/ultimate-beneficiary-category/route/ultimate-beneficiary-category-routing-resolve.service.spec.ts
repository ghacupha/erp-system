jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUltimateBeneficiaryCategory, UltimateBeneficiaryCategory } from '../ultimate-beneficiary-category.model';
import { UltimateBeneficiaryCategoryService } from '../service/ultimate-beneficiary-category.service';

import { UltimateBeneficiaryCategoryRoutingResolveService } from './ultimate-beneficiary-category-routing-resolve.service';

describe('UltimateBeneficiaryCategory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UltimateBeneficiaryCategoryRoutingResolveService;
  let service: UltimateBeneficiaryCategoryService;
  let resultUltimateBeneficiaryCategory: IUltimateBeneficiaryCategory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(UltimateBeneficiaryCategoryRoutingResolveService);
    service = TestBed.inject(UltimateBeneficiaryCategoryService);
    resultUltimateBeneficiaryCategory = undefined;
  });

  describe('resolve', () => {
    it('should return IUltimateBeneficiaryCategory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUltimateBeneficiaryCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUltimateBeneficiaryCategory).toEqual({ id: 123 });
    });

    it('should return new IUltimateBeneficiaryCategory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUltimateBeneficiaryCategory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUltimateBeneficiaryCategory).toEqual(new UltimateBeneficiaryCategory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UltimateBeneficiaryCategory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUltimateBeneficiaryCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUltimateBeneficiaryCategory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
