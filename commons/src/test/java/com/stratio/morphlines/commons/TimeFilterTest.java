/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.morphlines.commons;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

@RunWith(JUnit4.class)
public class TimeFilterTest extends BaseTest{

    private static final String MORPH_CONF_FILE = "/timefilter/timeFilter.conf";

    private Config config;

    @Before
    public void setUp() throws IOException {
        config = parse(MORPH_CONF_FILE).getConfigList("commands").get(0).getConfig("timeFilter");
    }

    @Test
    public void testFilterOk() {

        final MorphlineContext context = new MorphlineContext.Builder().build();
        Collector collectorParent = new Collector();
        Collector collectorChild = new Collector();
        final Command command = new TimeFilterBuilder().build(config, collectorParent,
                collectorChild, context);

        Record record = new Record();
        record.put("createdAt", "21/01/2014");
        
        command.process(record);
        
        List<Record> records = collectorChild.getRecords();
        assertThat(records.size()).isGreaterThan(0);
    }

    @Test
    public void testDiscardRecord() {
        final MorphlineContext context = new MorphlineContext.Builder().build();
        Collector collectorParent = new Collector();
        Collector collectorChild = new Collector();
        final Command command = new TimeFilterBuilder().build(config, collectorParent,
                collectorChild, context);

        Record record = new Record();
        record.put("createdAt", "21/01/2015");
        
        command.process(record);
        
        List<Record> records = collectorChild.getRecords();
        assertThat(records.size()).isEqualTo(0);
    }


}
