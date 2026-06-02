jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AcquiringIssuingFlagService } from '../service/acquiring-issuing-flag.service';
import { IAcquiringIssuingFlag, AcquiringIssuingFlag } from '../acquiring-issuing-flag.model';

import { AcquiringIssuingFlagUpdateComponent } from './acquiring-issuing-flag-update.component';

describe('AcquiringIssuingFlag Management Update Component', () => {
  let comp: AcquiringIssuingFlagUpdateComponent;
  let fixture: ComponentFixture<AcquiringIssuingFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let acquiringIssuingFlagService: AcquiringIssuingFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AcquiringIssuingFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AcquiringIssuingFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcquiringIssuingFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    acquiringIssuingFlagService = TestBed.inject(AcquiringIssuingFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const acquiringIssuingFlag: IAcquiringIssuingFlag = { id: 456 };

      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(acquiringIssuingFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcquiringIssuingFlag>>();
      const acquiringIssuingFlag = { id: 123 };
      jest.spyOn(acquiringIssuingFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acquiringIssuingFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(acquiringIssuingFlagService.update).toHaveBeenCalledWith(acquiringIssuingFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcquiringIssuingFlag>>();
      const acquiringIssuingFlag = new AcquiringIssuingFlag();
      jest.spyOn(acquiringIssuingFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acquiringIssuingFlag }));
      saveSubject.complete();

      // THEN
      expect(acquiringIssuingFlagService.create).toHaveBeenCalledWith(acquiringIssuingFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcquiringIssuingFlag>>();
      const acquiringIssuingFlag = { id: 123 };
      jest.spyOn(acquiringIssuingFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(acquiringIssuingFlagService.update).toHaveBeenCalledWith(acquiringIssuingFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
