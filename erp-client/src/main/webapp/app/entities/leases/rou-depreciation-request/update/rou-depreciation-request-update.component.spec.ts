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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RouDepreciationRequestService } from '../service/rou-depreciation-request.service';
import { IRouDepreciationRequest, RouDepreciationRequest } from '../rou-depreciation-request.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

import { RouDepreciationRequestUpdateComponent } from './rou-depreciation-request-update.component';

describe('RouDepreciationRequest Management Update Component', () => {
  let comp: RouDepreciationRequestUpdateComponent;
  let fixture: ComponentFixture<RouDepreciationRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouDepreciationRequestService: RouDepreciationRequestService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouDepreciationRequestUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouDepreciationRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouDepreciationRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouDepreciationRequestService = TestBed.inject(RouDepreciationRequestService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const rouDepreciationRequest: IRouDepreciationRequest = { id: 456 };
      const initiatedBy: IApplicationUser = { id: 47118 };
      rouDepreciationRequest.initiatedBy = initiatedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 76892 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [initiatedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouDepreciationRequest });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouDepreciationRequest: IRouDepreciationRequest = { id: 456 };
      const initiatedBy: IApplicationUser = { id: 20343 };
      rouDepreciationRequest.initiatedBy = initiatedBy;

      activatedRoute.data = of({ rouDepreciationRequest });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouDepreciationRequest));
      expect(comp.applicationUsersSharedCollection).toContain(initiatedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouDepreciationRequest>>();
      const rouDepreciationRequest = { id: 123 };
      jest.spyOn(rouDepreciationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouDepreciationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouDepreciationRequest }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouDepreciationRequestService.update).toHaveBeenCalledWith(rouDepreciationRequest);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouDepreciationRequest>>();
      const rouDepreciationRequest = new RouDepreciationRequest();
      jest.spyOn(rouDepreciationRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouDepreciationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouDepreciationRequest }));
      saveSubject.complete();

      // THEN
      expect(rouDepreciationRequestService.create).toHaveBeenCalledWith(rouDepreciationRequest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouDepreciationRequest>>();
      const rouDepreciationRequest = { id: 123 };
      jest.spyOn(rouDepreciationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouDepreciationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouDepreciationRequestService.update).toHaveBeenCalledWith(rouDepreciationRequest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
