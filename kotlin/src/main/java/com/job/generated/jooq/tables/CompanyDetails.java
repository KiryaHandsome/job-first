/*
 * This file is generated by jOOQ.
 */
package com.job.generated.jooq.tables;


import com.job.generated.jooq.Keys;
import com.job.generated.jooq.Public;
import com.job.generated.jooq.tables.records.CompanyDetailsRecord;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class CompanyDetails extends TableImpl<CompanyDetailsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.company_details</code>
     */
    public static final CompanyDetails COMPANY_DETAILS = new CompanyDetails();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CompanyDetailsRecord> getRecordType() {
        return CompanyDetailsRecord.class;
    }

    /**
     * The column <code>public.company_details.user_id</code>.
     */
    public final TableField<CompanyDetailsRecord, UUID> USER_ID = createField(DSL.name("user_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.company_details.office_location</code>.
     */
    public final TableField<CompanyDetailsRecord, String> OFFICE_LOCATION = createField(DSL.name("office_location"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.company_details.employees_amount</code>.
     */
    public final TableField<CompanyDetailsRecord, Integer> EMPLOYEES_AMOUNT = createField(DSL.name("employees_amount"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.company_details.description</code>.
     */
    public final TableField<CompanyDetailsRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.company_details.founded_at</code>.
     */
    public final TableField<CompanyDetailsRecord, LocalDate> FOUNDED_AT = createField(DSL.name("founded_at"), SQLDataType.LOCALDATE, this, "");

    private CompanyDetails(Name alias, Table<CompanyDetailsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private CompanyDetails(Name alias, Table<CompanyDetailsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.company_details</code> table reference
     */
    public CompanyDetails(String alias) {
        this(DSL.name(alias), COMPANY_DETAILS);
    }

    /**
     * Create an aliased <code>public.company_details</code> table reference
     */
    public CompanyDetails(Name alias) {
        this(alias, COMPANY_DETAILS);
    }

    /**
     * Create a <code>public.company_details</code> table reference
     */
    public CompanyDetails() {
        this(DSL.name("company_details"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<CompanyDetailsRecord> getPrimaryKey() {
        return Keys.COMPANY_DETAILS_PKEY;
    }

    @Override
    public CompanyDetails as(String alias) {
        return new CompanyDetails(DSL.name(alias), this);
    }

    @Override
    public CompanyDetails as(Name alias) {
        return new CompanyDetails(alias, this);
    }

    @Override
    public CompanyDetails as(Table<?> alias) {
        return new CompanyDetails(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public CompanyDetails rename(String name) {
        return new CompanyDetails(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CompanyDetails rename(Name name) {
        return new CompanyDetails(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public CompanyDetails rename(Table<?> name) {
        return new CompanyDetails(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CompanyDetails where(Condition condition) {
        return new CompanyDetails(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CompanyDetails where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CompanyDetails where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CompanyDetails where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CompanyDetails where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CompanyDetails where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CompanyDetails where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CompanyDetails where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CompanyDetails whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CompanyDetails whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
