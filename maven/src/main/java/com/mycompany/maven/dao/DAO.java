package com.mycompany.maven.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Thadeu
 * @param <T>
 */
public abstract class DAO<T> {

    @PersistenceContext(unitName = "com.mycompany_maven_war_1PU")
    protected EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    private Class<T> entityClass;

    @PostConstruct
    public void init() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        this.entityClass = (Class<T>) actualTypeArguments[0];
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void clearCache() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void detach(T entity) {
        getEntityManager().detach(entity);
    }

    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int first, int pageSize) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);

        return q.getResultList();
    }

    public T findByProperty(String property, Object value) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);

        String[] cols = property.split("\\.");
        Path<Object> path = from.get(cols[0]);
        for (int i = 1; i < cols.length; i++) {
            path = path.get(cols[i]);
        }

        cq.where(cb.equal(path, value));

        T singleResult;
        try {
            singleResult = getEntityManager().createQuery(cq).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            singleResult = null;
        }
        return singleResult;
    }

    public T findByProperties(Map<String, Object> properties) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);
        cq.where(cb.and(generateRestrictions(from, cb, properties)));

        T singleResult;
        try {
            singleResult = getEntityManager().createQuery(cq).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            singleResult = null;
        }
        return singleResult;
    }

    public List<T> listByProperty(String property, Object value) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);

        String[] cols = property.split("\\.");
        Path<Object> path = from.get(cols[0]);
        for (int i = 1; i < cols.length; i++) {
            path = path.get(cols[i]);
        }

        cq.where(cb.equal(path, value));

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> listByProperties(Map<String, Object> properties) {
        return listByProperties(properties, true);
    }

    public List<T> listByProperties(Map<String, Object> properties, boolean distinct) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);
        cq.distinct(distinct);
        cq.where(cb.and(generateRestrictions(from, cb, properties)));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public Predicate[] generateRestrictions(Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, Object> filters) {
        List<Predicate> predicates = new ArrayList<>();
        if (filters != null) {
            for (String key : filters.keySet()) {
                Path<?> path = root;

                String[] matches = key.split("\\.");
                for (String match : matches) {
                    path = path.get(match);
                }

                Object value = filters.get(key);

                if (value == null) {
                    predicates.add(criteriaBuilder.isNull(path));
                } else if (value.toString().contains("%")) {
                    predicates.add(criteriaBuilder.like(path.as(String.class), value.toString()));
                } else {
                    predicates.add(criteriaBuilder.equal(path, value));
                }
            }
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    public void getMetamodel(Root<T> root) {
    }

    public List<T> findRange(int first, int pageSize, String sortField, boolean asc, Map<String, String> filters) {
        CriteriaBuilder criteruaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteruaBuilder.createQuery(entityClass);

        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        if (filters != null) {
            Iterator<String> iteratorKeys = filters.keySet().iterator();

            while (iteratorKeys.hasNext()) {
                Path<?> path = root;
                String filterKey = iteratorKeys.next();

                String[] filterSplit = filterKey.split("\\.");
                for (String split : filterSplit) {
                    path = path.get(split);
                }
                criteriaQuery.where(criteruaBuilder.equal(path, filters.get(filterKey)));
            }
        }

        if (sortField != null) {
            Path<?> path = root;
            String[] sortCols = sortField.split("\\.");
            for (String sc : sortCols) {
                path = path.get(sc);
            }
            if (asc) {
                criteriaQuery.orderBy(criteruaBuilder.asc(path));
            } else {
                criteriaQuery.orderBy(criteruaBuilder.desc(path));
            }
        }

        Query q = getEntityManager().createQuery(criteriaQuery);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);

        return q.getResultList();
    }

    public List<T> findRange(int first, int pageSize, String sortField, boolean asc, Map<String, String> filters, boolean orFilter) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);

        Root<T> root = cq.from(entityClass);
        cq.select(root);

        if (filters != null) {
            if (orFilter) {
                cq.where(cb.or(generateRestrictions(root, cb, Collections.<String, Object>unmodifiableMap(filters))));
            } else {
                cq.where(cb.and(generateRestrictions(root, cb, Collections.<String, Object>unmodifiableMap(filters))));
            }
        }

        if (sortField != null) {
            Path<?> path = root;
            String[] sortCols = sortField.split("\\.");
            for (String sc : sortCols) {
                path = path.get(sc);
            }
            if (asc) {
                cq.orderBy(cb.asc(path));
            } else {
                cq.orderBy(cb.desc(path));
            }
        }

        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);

        return q.getResultList();
    }

    public T findSingleByQuery(String queryName, String... params) {
        TypedQuery<T> namedQuery = getEntityManager().createNamedQuery(queryName, entityClass);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                namedQuery.setParameter(i + 1, params[i]);
            }
        }
        return namedQuery.getSingleResult();
    }

    public List<T> findByQuery(String queryName, String... params) {
        TypedQuery<T> namedQuery = getEntityManager().createNamedQuery(queryName, entityClass);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                namedQuery.setParameter(i + 1, params[i]);
            }
        }
        return namedQuery.getResultList();
    }

    public int count(Map<String, String> filters) {
        CriteriaBuilder criteruaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteruaBuilder.createQuery(Long.class);

        Root<T> root = criteria.from(entityClass);
        criteria.select(criteruaBuilder.count(root));

        if (filters != null) {
            Iterator<String> iteratorKeys = filters.keySet().iterator();

            while (iteratorKeys.hasNext()) {
                Path<?> path = root;
                String filterKey = iteratorKeys.next();

                String[] filterSplit = filterKey.split("\\.");
                for (String split : filterSplit) {
                    path = path.get(split);
                }
                criteria.where(criteruaBuilder.equal(path, filters.get(filterKey)));
            }
        }

        Query q = getEntityManager().createQuery(criteria);

        return ((Long) q.getSingleResult()).intValue();
    }
}
