jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WIPListReportService } from '../service/wip-list-report.service';
import { IWIPListReport, WIPListReport } from '../wip-list-report.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

import { WIPListReportUpdateComponent } from './wip-list-report-update.component';

describe('WIPListReport Management Update Component', () => {
  let comp: WIPListReportUpdateComponent;
  let fixture: ComponentFixture<WIPListReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wIPListReportService: WIPListReportService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WIPListReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(WIPListReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WIPListReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wIPListReportService = TestBed.inject(WIPListReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const wIPListReport: IWIPListReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 6970 };
      wIPListReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 89487 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wIPListReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const wIPListReport: IWIPListReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 54622 };
      wIPListReport.requestedBy = requestedBy;

      activatedRoute.data = of({ wIPListReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(wIPListReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WIPListReport>>();
      const wIPListReport = { id: 123 };
      jest.spyOn(wIPListReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wIPListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wIPListReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(wIPListReportService.update).toHaveBeenCalledWith(wIPListReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WIPListReport>>();
      const wIPListReport = new WIPListReport();
      jest.spyOn(wIPListReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wIPListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wIPListReport }));
      saveSubject.complete();

      // THEN
      expect(wIPListReportService.create).toHaveBeenCalledWith(wIPListReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WIPListReport>>();
      const wIPListReport = { id: 123 };
      jest.spyOn(wIPListReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wIPListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wIPListReportService.update).toHaveBeenCalledWith(wIPListReport);
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
