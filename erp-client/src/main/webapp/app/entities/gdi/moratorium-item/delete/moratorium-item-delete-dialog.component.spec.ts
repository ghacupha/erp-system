jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MoratoriumItemService } from '../service/moratorium-item.service';

import { MoratoriumItemDeleteDialogComponent } from './moratorium-item-delete-dialog.component';

describe('MoratoriumItem Management Delete Component', () => {
  let comp: MoratoriumItemDeleteDialogComponent;
  let fixture: ComponentFixture<MoratoriumItemDeleteDialogComponent>;
  let service: MoratoriumItemService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MoratoriumItemDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(MoratoriumItemDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MoratoriumItemDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MoratoriumItemService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
