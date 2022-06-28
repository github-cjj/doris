// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.catalog;

import org.apache.doris.alter.AlterCancelException;
import org.apache.doris.common.DdlException;
import org.apache.doris.common.MetaNotFoundException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface TableIf {

    void readLock();

    boolean tryReadLock(long timeout, TimeUnit unit);

    void readUnlock();

    void writeLock();

    boolean writeLockIfExist();

    boolean tryWriteLock(long timeout, TimeUnit unit);

    void writeUnlock();

    boolean isWriteLockHeldByCurrentThread();

    <E extends Exception> void writeLockOrException(E e) throws E;

    void writeLockOrDdlException() throws DdlException;

    void writeLockOrMetaException() throws MetaNotFoundException;

    void writeLockOrAlterCancelException() throws AlterCancelException;

    boolean tryWriteLockOrMetaException(long timeout, TimeUnit unit) throws MetaNotFoundException;

    <E extends Exception> boolean tryWriteLockOrException(long timeout, TimeUnit unit, E e) throws E;

    boolean tryWriteLockIfExist(long timeout, TimeUnit unit);

    long getId();

    String getName();

    TableType getType();

    List<Column> getFullSchema();

    List<Column> getBaseSchema();

    List<Column> getBaseSchema(boolean full);

    void setNewFullSchema(List<Column> newSchema);

    Column getColumn(String name);

    String getMysqlType();

    String getEngine();

    String getComment();

    long getCreateTime();

    long getUpdateTime();

    long getRowCount();

    long getDataLength();

    long getAvgRowLength();

    long getLastCheckTime();

    String getComment(boolean escapeQuota);

    /**
     * Doris table type.
     */
    public enum TableType {
        MYSQL, ODBC, OLAP, SCHEMA, INLINE_VIEW, VIEW, BROKER, ELASTICSEARCH, HIVE, ICEBERG, HUDI, TABLE_VALUED_FUNCTION;

        public String toEngineName() {
            switch (this) {
                case MYSQL:
                    return "MySQL";
                case ODBC:
                    return "Odbc";
                case OLAP:
                    return "Doris";
                case SCHEMA:
                    return "MEMORY";
                case INLINE_VIEW:
                    return "InlineView";
                case VIEW:
                    return "View";
                case BROKER:
                    return "Broker";
                case ELASTICSEARCH:
                    return "ElasticSearch";
                case HIVE:
                    return "Hive";
                case HUDI:
                    return "Hudi";
                case TABLE_VALUED_FUNCTION:
                    return "Table_Valued_Function";
                default:
                    return null;
            }
        }

        public String toMysqlType() {
            switch (this) {
                case OLAP:
                    return "BASE TABLE";
                case SCHEMA:
                    return "SYSTEM VIEW";
                case INLINE_VIEW:
                case VIEW:
                    return "VIEW";
                case MYSQL:
                case ODBC:
                case BROKER:
                case ELASTICSEARCH:
                case HIVE:
                case HUDI:
                case TABLE_VALUED_FUNCTION:
                    return "EXTERNAL TABLE";
                default:
                    return null;
            }
        }
    }
}
