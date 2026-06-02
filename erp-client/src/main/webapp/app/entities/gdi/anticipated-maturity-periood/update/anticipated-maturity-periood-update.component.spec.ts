jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AnticipatedMaturityPerioodService } from '../service/anticipated-maturity-periood.service';
import { IAnticipatedMaturityPeriood, AnticipatedMaturityPeriood } from '../anticipated-maturity-periood.model';

import { AnticipatedMaturityPerioodUpdateComponent } from './anticipated-maturity-periood-update.component';

describe('AnticipatedMaturityPeriood Management Update Component', () => {
  let comp: AnticipatedMaturityPerioodUpdateComponent;
  let fixture: ComponentFixture<AnticipatedMaturityPerioodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let anticipatedMaturityPerioodService: AnticipatedMaturityPerioodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AnticipatedMaturityPerioodUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AnticipatedMaturityPerioodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnticipatedMaturityPerioodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    anticipatedMaturityPerioodService = TestBed.inject(AnticipatedMaturityPerioodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const anticipatedMaturityPeriood: IAnticipatedMaturityPeriood = { id: 456 };

      activatedRoute.data = of({ anticipatedMaturityPeriood });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(anticipatedMaturityPeriood));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AnticipatedMaturityPeriood>>();
      const anticipatedMaturityPeriood = { id: 123 };
      jest.spyOn(anticipatedMaturityPerioodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anticipatedMaturityPeriood });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: anticipatedMaturityPeriood }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(anticipatedMaturityPerioodService.update).toHaveBeenCalledWith(anticipatedMaturityPeriood);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AnticipatedMaturityPeriood>>();
      const anticipatedMaturityPeriood = new AnticipatedMaturityPeriood();
      jest.spyOn(anticipatedMaturityPerioodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anticipatedMaturityPeriood });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: anticipatedMaturityPeriood }));
      saveSubject.complete();

      // THEN
      expect(anticipatedMaturityPerioodService.create).toHaveBeenCalledWith(anticipatedMaturityPeriood);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AnticipatedMaturityPeriood>>();
      const anticipatedMaturityPeriood = { id: 123 };
      jest.spyOn(anticipatedMaturityPerioodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anticipatedMaturityPeriood });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(anticipatedMaturityPerioodService.update).toHaveBeenCalledWith(anticipatedMaturityPeriood);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
