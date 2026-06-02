jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbCreditApplicationStatusService } from '../service/crb-credit-application-status.service';
import { ICrbCreditApplicationStatus, CrbCreditApplicationStatus } from '../crb-credit-application-status.model';

import { CrbCreditApplicationStatusUpdateComponent } from './crb-credit-application-status-update.component';

describe('CrbCreditApplicationStatus Management Update Component', () => {
  let comp: CrbCreditApplicationStatusUpdateComponent;
  let fixture: ComponentFixture<CrbCreditApplicationStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbCreditApplicationStatusService: CrbCreditApplicationStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbCreditApplicationStatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbCreditApplicationStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbCreditApplicationStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbCreditApplicationStatusService = TestBed.inject(CrbCreditApplicationStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbCreditApplicationStatus: ICrbCreditApplicationStatus = { id: 456 };

      activatedRoute.data = of({ crbCreditApplicationStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbCreditApplicationStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbCreditApplicationStatus>>();
      const crbCreditApplicationStatus = { id: 123 };
      jest.spyOn(crbCreditApplicationStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbCreditApplicationStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbCreditApplicationStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbCreditApplicationStatusService.update).toHaveBeenCalledWith(crbCreditApplicationStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbCreditApplicationStatus>>();
      const crbCreditApplicationStatus = new CrbCreditApplicationStatus();
      jest.spyOn(crbCreditApplicationStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbCreditApplicationStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbCreditApplicationStatus }));
      saveSubject.complete();

      // THEN
      expect(crbCreditApplicationStatusService.create).toHaveBeenCalledWith(crbCreditApplicationStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbCreditApplicationStatus>>();
      const crbCreditApplicationStatus = { id: 123 };
      jest.spyOn(crbCreditApplicationStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbCreditApplicationStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbCreditApplicationStatusService.update).toHaveBeenCalledWith(crbCreditApplicationStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
