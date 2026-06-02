jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SecurityTenureService } from '../service/security-tenure.service';
import { ISecurityTenure, SecurityTenure } from '../security-tenure.model';

import { SecurityTenureUpdateComponent } from './security-tenure-update.component';

describe('SecurityTenure Management Update Component', () => {
  let comp: SecurityTenureUpdateComponent;
  let fixture: ComponentFixture<SecurityTenureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let securityTenureService: SecurityTenureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SecurityTenureUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SecurityTenureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SecurityTenureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    securityTenureService = TestBed.inject(SecurityTenureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const securityTenure: ISecurityTenure = { id: 456 };

      activatedRoute.data = of({ securityTenure });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(securityTenure));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SecurityTenure>>();
      const securityTenure = { id: 123 };
      jest.spyOn(securityTenureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityTenure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityTenure }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(securityTenureService.update).toHaveBeenCalledWith(securityTenure);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SecurityTenure>>();
      const securityTenure = new SecurityTenure();
      jest.spyOn(securityTenureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityTenure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityTenure }));
      saveSubject.complete();

      // THEN
      expect(securityTenureService.create).toHaveBeenCalledWith(securityTenure);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SecurityTenure>>();
      const securityTenure = { id: 123 };
      jest.spyOn(securityTenureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityTenure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(securityTenureService.update).toHaveBeenCalledWith(securityTenure);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
