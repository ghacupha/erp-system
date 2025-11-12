///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { Component, ElementRef, Input, Renderer2 } from '@angular/core';

@Component({
  selector: 'jhi-password-strength-bar',
  templateUrl: './password-strength-bar.component.html',
  styleUrls: ['./password-strength-bar.component.scss'],
})
export class PasswordStrengthBarComponent {
  colors = ['#F00', '#F90', '#FF0', '#9F0', '#0F0'];

  constructor(private renderer: Renderer2, private elementRef: ElementRef) {}

  measureStrength(p: string): number {
    let force = 0;
    const regex = /[$-/:-?{-~!"^_`[\]]/g; // "
    const lowerLetters = /[a-z]+/.test(p);
    const upperLetters = /[A-Z]+/.test(p);
    const numbers = /[0-9]+/.test(p);
    const symbols = regex.test(p);

    const flags = [lowerLetters, upperLetters, numbers, symbols];
    const passedMatches = flags.filter((isMatchedFlag: boolean) => isMatchedFlag === true).length;

    force += 2 * p.length + (p.length >= 10 ? 1 : 0);
    force += passedMatches * 10;

    // penalty (short password)
    force = p.length <= 6 ? Math.min(force, 10) : force;

    // penalty (poor variety of characters)
    force = passedMatches === 1 ? Math.min(force, 10) : force;
    force = passedMatches === 2 ? Math.min(force, 20) : force;
    force = passedMatches === 3 ? Math.min(force, 40) : force;

    return force;
  }

  getColor(s: number): { idx: number; color: string } {
    let idx = 0;
    if (s > 10) {
      if (s <= 20) {
        idx = 1;
      } else if (s <= 30) {
        idx = 2;
      } else if (s <= 40) {
        idx = 3;
      } else {
        idx = 4;
      }
    }
    return { idx: idx + 1, color: this.colors[idx] };
  }

  @Input()
  set passwordToCheck(password: string) {
    if (password) {
      const c = this.getColor(this.measureStrength(password));
      const element = this.elementRef.nativeElement;
      if (element.className) {
        this.renderer.removeClass(element, element.className);
      }
      const lis = element.getElementsByTagName('li');
      for (let i = 0; i < lis.length; i++) {
        if (i < c.idx) {
          this.renderer.setStyle(lis[i], 'backgroundColor', c.color);
        } else {
          this.renderer.setStyle(lis[i], 'backgroundColor', '#DDD');
        }
      }
    }
  }
}
