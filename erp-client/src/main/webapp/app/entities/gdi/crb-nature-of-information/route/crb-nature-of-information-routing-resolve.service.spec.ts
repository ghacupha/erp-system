///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbNatureOfInformation, CrbNatureOfInformation } from '../crb-nature-of-information.model';
import { CrbNatureOfInformationService } from '../service/crb-nature-of-information.service';

import { CrbNatureOfInformationRoutingResolveService } from './crb-nature-of-information-routing-resolve.service';

describe('CrbNatureOfInformation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbNatureOfInformationRoutingResolveService;
  let service: CrbNatureOfInformationService;
  let resultCrbNatureOfInformation: ICrbNatureOfInformation | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbNatureOfInformationRoutingResolveService);
    service = TestBed.inject(CrbNatureOfInformationService);
    resultCrbNatureOfInformation = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbNatureOfInformation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbNatureOfInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbNatureOfInformation).toEqual({ id: 123 });
    });

    it('should return new ICrbNatureOfInformation if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbNatureOfInformation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbNatureOfInformation).toEqual(new CrbNatureOfInformation());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbNatureOfInformation })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbNatureOfInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbNatureOfInformation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
