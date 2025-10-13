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

import { Injectable } from '@angular/core';

/**
 * An utility service for link parsing.
 */
@Injectable({
  providedIn: 'root',
})
export class ParseLinks {
  /**
   * Method to parse the links
   */
  parse(header: string): { [key: string]: number } {
    if (header.length === 0) {
      throw new Error('input must not be of zero length');
    }

    // Split parts by comma
    const parts: string[] = header.split(',');
    const links: { [key: string]: number } = {};

    // Parse each part into a named link
    parts.forEach(p => {
      const section: string[] = p.split(';');

      if (section.length !== 2) {
        throw new Error('section could not be split on ";"');
      }

      const url: string = section[0].replace(/<(.*)>/, '$1').trim();
      const queryString: { [key: string]: string | undefined } = {};

      url.replace(
        new RegExp('([^?=&]+)(=([^&]*))?', 'g'),
        ($0: string, $1: string | undefined, $2: string | undefined, $3: string | undefined) => {
          if ($1 !== undefined) {
            queryString[$1] = $3;
          }
          return $3 ?? '';
        }
      );

      if (queryString.page !== undefined) {
        const name: string = section[1].replace(/rel="(.*)"/, '$1').trim();
        links[name] = parseInt(queryString.page, 10);
      }
    });
    return links;
  }
}
