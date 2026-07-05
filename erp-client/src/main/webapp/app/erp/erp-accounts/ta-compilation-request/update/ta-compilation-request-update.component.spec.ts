///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { TACompilationRequestService } from '../service/ta-compilation-request.service';
import { ITACompilationRequest, TACompilationRequest } from '../ta-compilation-request.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { TACompilationRequestUpdateComponent } from './ta-compilation-request-update.component';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

describe('TACompilationRequest Management Update Component', () => {
  let comp: TACompilationRequestUpdateComponent;
  let fixture: ComponentFixture<TACompilationRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tACompilationRequestService: TACompilationRequestService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TACompilationRequestUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TACompilationRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TACompilationRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tACompilationRequestService = TestBed.inject(TACompilationRequestService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const tACompilationRequest: ITACompilationRequest = { id: 456 };
      const initiatedBy: IApplicationUser = { id: 2633 };
      tACompilationRequest.initiatedBy = initiatedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 4444 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [initiatedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tACompilationRequest });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tACompilationRequest: ITACompilationRequest = { id: 456 };
      const initiatedBy: IApplicationUser = { id: 47331 };
      tACompilationRequest.initiatedBy = initiatedBy;

      activatedRoute.data = of({ tACompilationRequest });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tACompilationRequest));
      expect(comp.applicationUsersSharedCollection).toContain(initiatedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TACompilationRequest>>();
      const tACompilationRequest = { id: 123 };
      jest.spyOn(tACompilationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tACompilationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tACompilationRequest }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tACompilationRequestService.update).toHaveBeenCalledWith(tACompilationRequest);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TACompilationRequest>>();
      const tACompilationRequest = new TACompilationRequest();
      jest.spyOn(tACompilationRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tACompilationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tACompilationRequest }));
      saveSubject.complete();

      // THEN
      expect(tACompilationRequestService.create).toHaveBeenCalledWith(tACompilationRequest);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TACompilationRequest>>();
      const tACompilationRequest = { id: 123 };
      jest.spyOn(tACompilationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tACompilationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tACompilationRequestService.update).toHaveBeenCalledWith(tACompilationRequest);
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
