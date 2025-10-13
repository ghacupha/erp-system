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

import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WIPTransferListReportService } from '../service/wip-transfer-list-report.service';
import { IWIPTransferListReport, WIPTransferListReport } from '../wip-transfer-list-report.model';

import { WIPTransferListReportUpdateComponent } from './wip-transfer-list-report-update.component';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';

describe('WIPTransferListReport Management Update Component', () => {
  let comp: WIPTransferListReportUpdateComponent;
  let fixture: ComponentFixture<WIPTransferListReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wIPTransferListReportService: WIPTransferListReportService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WIPTransferListReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(WIPTransferListReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WIPTransferListReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wIPTransferListReportService = TestBed.inject(WIPTransferListReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const wIPTransferListReport: IWIPTransferListReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 61585 };
      wIPTransferListReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 49026 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wIPTransferListReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const wIPTransferListReport: IWIPTransferListReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 34428 };
      wIPTransferListReport.requestedBy = requestedBy;

      activatedRoute.data = of({ wIPTransferListReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(wIPTransferListReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WIPTransferListReport>>();
      const wIPTransferListReport = { id: 123 };
      jest.spyOn(wIPTransferListReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wIPTransferListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wIPTransferListReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(wIPTransferListReportService.update).toHaveBeenCalledWith(wIPTransferListReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WIPTransferListReport>>();
      const wIPTransferListReport = new WIPTransferListReport();
      jest.spyOn(wIPTransferListReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wIPTransferListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wIPTransferListReport }));
      saveSubject.complete();

      // THEN
      expect(wIPTransferListReportService.create).toHaveBeenCalledWith(wIPTransferListReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WIPTransferListReport>>();
      const wIPTransferListReport = { id: 123 };
      jest.spyOn(wIPTransferListReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wIPTransferListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wIPTransferListReportService.update).toHaveBeenCalledWith(wIPTransferListReport);
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
