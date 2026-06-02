jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LegalStatusService } from '../service/legal-status.service';
import { ILegalStatus, LegalStatus } from '../legal-status.model';

import { LegalStatusUpdateComponent } from './legal-status-update.component';

describe('LegalStatus Management Update Component', () => {
  let comp: LegalStatusUpdateComponent;
  let fixture: ComponentFixture<LegalStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let legalStatusService: LegalStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LegalStatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LegalStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LegalStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    legalStatusService = TestBed.inject(LegalStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const legalStatus: ILegalStatus = { id: 456 };

      activatedRoute.data = of({ legalStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(legalStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LegalStatus>>();
      const legalStatus = { id: 123 };
      jest.spyOn(legalStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ legalStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: legalStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(legalStatusService.update).toHaveBeenCalledWith(legalStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LegalStatus>>();
      const legalStatus = new LegalStatus();
      jest.spyOn(legalStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ legalStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: legalStatus }));
      saveSubject.complete();

      // THEN
      expect(legalStatusService.create).toHaveBeenCalledWith(legalStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LegalStatus>>();
      const legalStatus = { id: 123 };
      jest.spyOn(legalStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ legalStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(legalStatusService.update).toHaveBeenCalledWith(legalStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
