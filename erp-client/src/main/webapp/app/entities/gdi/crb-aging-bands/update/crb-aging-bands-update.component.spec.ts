jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbAgingBandsService } from '../service/crb-aging-bands.service';
import { ICrbAgingBands, CrbAgingBands } from '../crb-aging-bands.model';

import { CrbAgingBandsUpdateComponent } from './crb-aging-bands-update.component';

describe('CrbAgingBands Management Update Component', () => {
  let comp: CrbAgingBandsUpdateComponent;
  let fixture: ComponentFixture<CrbAgingBandsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbAgingBandsService: CrbAgingBandsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbAgingBandsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbAgingBandsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbAgingBandsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbAgingBandsService = TestBed.inject(CrbAgingBandsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbAgingBands: ICrbAgingBands = { id: 456 };

      activatedRoute.data = of({ crbAgingBands });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbAgingBands));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAgingBands>>();
      const crbAgingBands = { id: 123 };
      jest.spyOn(crbAgingBandsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAgingBands });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbAgingBands }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbAgingBandsService.update).toHaveBeenCalledWith(crbAgingBands);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAgingBands>>();
      const crbAgingBands = new CrbAgingBands();
      jest.spyOn(crbAgingBandsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAgingBands });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbAgingBands }));
      saveSubject.complete();

      // THEN
      expect(crbAgingBandsService.create).toHaveBeenCalledWith(crbAgingBands);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAgingBands>>();
      const crbAgingBands = { id: 123 };
      jest.spyOn(crbAgingBandsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAgingBands });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbAgingBandsService.update).toHaveBeenCalledWith(crbAgingBands);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
