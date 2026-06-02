jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CardFraudInformationService } from '../service/card-fraud-information.service';
import { ICardFraudInformation, CardFraudInformation } from '../card-fraud-information.model';

import { CardFraudInformationUpdateComponent } from './card-fraud-information-update.component';

describe('CardFraudInformation Management Update Component', () => {
  let comp: CardFraudInformationUpdateComponent;
  let fixture: ComponentFixture<CardFraudInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardFraudInformationService: CardFraudInformationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardFraudInformationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CardFraudInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardFraudInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardFraudInformationService = TestBed.inject(CardFraudInformationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardFraudInformation: ICardFraudInformation = { id: 456 };

      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cardFraudInformation));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudInformation>>();
      const cardFraudInformation = { id: 123 };
      jest.spyOn(cardFraudInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardFraudInformation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardFraudInformationService.update).toHaveBeenCalledWith(cardFraudInformation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudInformation>>();
      const cardFraudInformation = new CardFraudInformation();
      jest.spyOn(cardFraudInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardFraudInformation }));
      saveSubject.complete();

      // THEN
      expect(cardFraudInformationService.create).toHaveBeenCalledWith(cardFraudInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CardFraudInformation>>();
      const cardFraudInformation = { id: 123 };
      jest.spyOn(cardFraudInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardFraudInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardFraudInformationService.update).toHaveBeenCalledWith(cardFraudInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
