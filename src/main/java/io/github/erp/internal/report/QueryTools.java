package io.github.erp.internal.report;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
