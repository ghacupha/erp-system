jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TaxRuleService } from '../service/tax-rule.service';
import { ITaxRule, TaxRule } from '../tax-rule.model';

import { TaxRuleUpdateComponent } from './tax-rule-update.component';

describe('Component Tests', () => {
  describe('TaxRule Management Update Component', () => {
    let comp: TaxRuleUpdateComponent;
    let fixture: ComponentFixture<TaxRuleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let taxRuleService: TaxRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaxRuleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TaxRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaxRuleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      taxRuleService = TestBed.inject(TaxRuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const taxRule: ITaxRule = { id: 456 };

        activatedRoute.data = of({ taxRule });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(taxRule));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaxRule>>();
        const taxRule = { id: 123 };
        jest.spyOn(taxRuleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taxRule }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(taxRuleService.update).toHaveBeenCalledWith(taxRule);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaxRule>>();
        const taxRule = new TaxRule();
        jest.spyOn(taxRuleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taxRule }));
        saveSubject.complete();

        // THEN
        expect(taxRuleService.create).toHaveBeenCalledWith(taxRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaxRule>>();
        const taxRule = { id: 123 };
        jest.spyOn(taxRuleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(taxRuleService.update).toHaveBeenCalledWith(taxRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
