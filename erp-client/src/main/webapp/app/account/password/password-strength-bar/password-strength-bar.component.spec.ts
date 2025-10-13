///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { PasswordStrengthBarComponent } from './password-strength-bar.component';

describe('PasswordStrengthBarComponent', () => {
  let comp: PasswordStrengthBarComponent;
  let fixture: ComponentFixture<PasswordStrengthBarComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        declarations: [PasswordStrengthBarComponent],
      })
        .overrideTemplate(PasswordStrengthBarComponent, '')
        .compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordStrengthBarComponent);
    comp = fixture.componentInstance;
  });

  describe('PasswordStrengthBarComponents', () => {
    it('should initialize with default values', () => {
      expect(comp.measureStrength('')).toBe(0);
      expect(comp.colors).toEqual(['#F00', '#F90', '#FF0', '#9F0', '#0F0']);
      expect(comp.getColor(0).idx).toBe(1);
      expect(comp.getColor(0).color).toBe(comp.colors[0]);
    });

    it('should increase strength upon password value change', () => {
      expect(comp.measureStrength('')).toBe(0);
      expect(comp.measureStrength('aa')).toBeGreaterThanOrEqual(comp.measureStrength(''));
      expect(comp.measureStrength('aa^6')).toBeGreaterThanOrEqual(comp.measureStrength('aa'));
      expect(comp.measureStrength('Aa090(**)')).toBeGreaterThanOrEqual(comp.measureStrength('aa^6'));
      expect(comp.measureStrength('Aa090(**)+-07365')).toBeGreaterThanOrEqual(comp.measureStrength('Aa090(**)'));
    });

    it('should change the color based on strength', () => {
      expect(comp.getColor(0).color).toBe(comp.colors[0]);
      expect(comp.getColor(11).color).toBe(comp.colors[1]);
      expect(comp.getColor(22).color).toBe(comp.colors[2]);
      expect(comp.getColor(33).color).toBe(comp.colors[3]);
      expect(comp.getColor(44).color).toBe(comp.colors[4]);
    });
  });
});
