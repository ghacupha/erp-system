/*
 * Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
 * Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// Load erp-client/.env if dotenv is available and the file exists.
// dotenv is optional — SYSTEM_BUILD can also be set directly in the shell.
try { require('dotenv').config(); } catch { /* dotenv not installed, skip */ }

module.exports = {
  I18N_HASH: 'generated_hash',
  // SERVER_API_URL: `"http://localhost:8980/"`,
  SERVER_API_URL: process.env.SERVER_API_URL,
  __VERSION__: process.env.hasOwnProperty('APP_VERSION') ? process.env.APP_VERSION : 'DEV',
  __DEBUG_INFO_ENABLED__: true,
  // 7-character git short hash injected at build time.
  // Set SYSTEM_BUILD in erp-client/.env or as a system/CI env var.
  // When absent, the footer falls back to the git-version.json hash.
  SYSTEM_BUILD: process.env.SYSTEM_BUILD || '',
};
