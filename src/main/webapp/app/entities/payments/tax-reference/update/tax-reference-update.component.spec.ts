jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TaxReferenceService } from '../service/tax-reference.service';
import { ITaxReference, TaxReference } from '../tax-reference.model';

import { TaxReferenceUpdateComponent } from './tax-reference-update.component';

describe('Component Tests', () => {
  describe('TaxReference Management Update Component', () => {
    let comp: TaxReferenceUpdateComponent;
    let fixture: ComponentFixture<TaxReferenceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let taxReferenceService: TaxReferenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaxReferenceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TaxReferenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaxReferenceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      taxReferenceService = TestBed.inject(TaxReferenceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const taxReference: ITaxReference = { id: 456 };

        activatedRoute.data = of({ taxReference });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(taxReference));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaxReference>>();
        const taxReference = { id: 123 };
        jest.spyOn(taxReferenceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxReference });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taxReference }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(taxReferenceService.update).toHaveBeenCalledWith(taxReference);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaxReference>>();
        const taxReference = new TaxReference();
        jest.spyOn(taxReferenceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxReference });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: taxReference }));
        saveSubject.complete();

        // THEN
        expect(taxReferenceService.create).toHaveBeenCalledWith(taxReference);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TaxReference>>();
        const taxReference = { id: 123 };
        jest.spyOn(taxReferenceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ taxReference });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(taxReferenceService.update).toHaveBeenCalledWith(taxReference);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
