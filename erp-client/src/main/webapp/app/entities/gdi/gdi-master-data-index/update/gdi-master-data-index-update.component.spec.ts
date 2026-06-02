jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GdiMasterDataIndexService } from '../service/gdi-master-data-index.service';
import { IGdiMasterDataIndex, GdiMasterDataIndex } from '../gdi-master-data-index.model';

import { GdiMasterDataIndexUpdateComponent } from './gdi-master-data-index-update.component';

describe('GdiMasterDataIndex Management Update Component', () => {
  let comp: GdiMasterDataIndexUpdateComponent;
  let fixture: ComponentFixture<GdiMasterDataIndexUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gdiMasterDataIndexService: GdiMasterDataIndexService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GdiMasterDataIndexUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(GdiMasterDataIndexUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GdiMasterDataIndexUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gdiMasterDataIndexService = TestBed.inject(GdiMasterDataIndexService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gdiMasterDataIndex: IGdiMasterDataIndex = { id: 456 };

      activatedRoute.data = of({ gdiMasterDataIndex });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(gdiMasterDataIndex));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GdiMasterDataIndex>>();
      const gdiMasterDataIndex = { id: 123 };
      jest.spyOn(gdiMasterDataIndexService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gdiMasterDataIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gdiMasterDataIndex }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(gdiMasterDataIndexService.update).toHaveBeenCalledWith(gdiMasterDataIndex);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GdiMasterDataIndex>>();
      const gdiMasterDataIndex = new GdiMasterDataIndex();
      jest.spyOn(gdiMasterDataIndexService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gdiMasterDataIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gdiMasterDataIndex }));
      saveSubject.complete();

      // THEN
      expect(gdiMasterDataIndexService.create).toHaveBeenCalledWith(gdiMasterDataIndex);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GdiMasterDataIndex>>();
      const gdiMasterDataIndex = { id: 123 };
      jest.spyOn(gdiMasterDataIndexService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gdiMasterDataIndex });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gdiMasterDataIndexService.update).toHaveBeenCalledWith(gdiMasterDataIndex);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
