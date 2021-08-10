jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FixedAssetAcquisitionService } from '../service/fixed-asset-acquisition.service';
import { IFixedAssetAcquisition, FixedAssetAcquisition } from '../fixed-asset-acquisition.model';

import { FixedAssetAcquisitionUpdateComponent } from './fixed-asset-acquisition-update.component';

describe('Component Tests', () => {
  describe('FixedAssetAcquisition Management Update Component', () => {
    let comp: FixedAssetAcquisitionUpdateComponent;
    let fixture: ComponentFixture<FixedAssetAcquisitionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fixedAssetAcquisitionService: FixedAssetAcquisitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FixedAssetAcquisitionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FixedAssetAcquisitionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixedAssetAcquisitionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fixedAssetAcquisitionService = TestBed.inject(FixedAssetAcquisitionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fixedAssetAcquisition: IFixedAssetAcquisition = { id: 456 };

        activatedRoute.data = of({ fixedAssetAcquisition });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fixedAssetAcquisition));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FixedAssetAcquisition>>();
        const fixedAssetAcquisition = { id: 123 };
        jest.spyOn(fixedAssetAcquisitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fixedAssetAcquisition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fixedAssetAcquisition }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fixedAssetAcquisitionService.update).toHaveBeenCalledWith(fixedAssetAcquisition);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FixedAssetAcquisition>>();
        const fixedAssetAcquisition = new FixedAssetAcquisition();
        jest.spyOn(fixedAssetAcquisitionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fixedAssetAcquisition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fixedAssetAcquisition }));
        saveSubject.complete();

        // THEN
        expect(fixedAssetAcquisitionService.create).toHaveBeenCalledWith(fixedAssetAcquisition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FixedAssetAcquisition>>();
        const fixedAssetAcquisition = { id: 123 };
        jest.spyOn(fixedAssetAcquisitionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fixedAssetAcquisition });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fixedAssetAcquisitionService.update).toHaveBeenCalledWith(fixedAssetAcquisition);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
