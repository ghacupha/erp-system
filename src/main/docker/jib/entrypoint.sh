#!/bin/sh
#
# Erp System - Mark III No 14 (Caleb Series) Server ver 1.1.4-SNAPSHOT
# Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#

export ERP_SERVER_DEPLOYMENT_PROD_PORT=${ERP_SERVER_DEPLOYMENT_PROD_PORT}

echo "The application will start in ${JHIPSTER_SLEEP}s on port configuration matrix: ${ERP_SERVER_DEPLOYMENT_PROD_PORT}. Standby..." && sleep ${JHIPSTER_SLEEP}
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "io.github.erp.ErpSystemApp"  "$@"
