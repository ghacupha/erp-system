package io.github.erp.internal.report;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import org.hibernate.Criteria;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * General utils used in tests and logs for checking queries created by JPA
 */
public class QueryTools {

    public static String toSQL(Criteria crit) {
        String sql = new BasicFormatterImpl().format(
            (new CriteriaJoinWalker(
                (OuterJoinLoadable)
                    ((CriteriaImpl)crit).getSession().getFactory().getEntityPersister(
                        ((CriteriaImpl)crit).getSession().getFactory().getImplementors(
                            ((CriteriaImpl)crit).getEntityOrClassName())[0]),
                new CriteriaQueryTranslator(
                    ((CriteriaImpl)crit).getSession().getFactory(),
                    ((CriteriaImpl)crit),
                    ((CriteriaImpl)crit).getEntityOrClassName(),
                    CriteriaQueryTranslator.ROOT_SQL_ALIAS),
                ((CriteriaImpl)crit).getSession().getFactory(),
                (CriteriaImpl)crit,
                ((CriteriaImpl)crit).getEntityOrClassName(),
                ((CriteriaImpl)crit).getSession().getLoadQueryInfluencers()
            )
            ).getSQLString()
        );

        return sql;
    }

    public static DataSource getDataSourceFromHibernateEntityManager(EntityManager em) {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        return info.getDataSource();
    }
}
