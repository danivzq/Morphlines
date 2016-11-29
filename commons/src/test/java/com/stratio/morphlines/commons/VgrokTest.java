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

import com.typesafe.config.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class VgrokTest extends BaseTest{
    
    private static final String MORPH_CONF_FILE = "/vgrok/vgrok.conf";
    
    private Config config;
    
    @Before
    public void setUp() throws IOException{
        config = parse(MORPH_CONF_FILE).getConfigList("commands").get(0).getConfig("vgrok");
    }
    
    @Test
    public void vgrokOK(){
        final MorphlineContext context = new MorphlineContext.Builder().build();
        Collector collectorParent = new Collector();
        Collector collectorChild = new Collector();
        final Command command = new VgrokBuilder().build(config, collectorParent,
                collectorChild, context);

        Record record = new Record();
        record.put("optionalHash", "");
        record.put("mandatoryHash", "8466a2b43729c29dcd7cc0fdfa1a9e7a");
        record.put("date", "1972-01-01T00:00:00+01:00");
        record.put("id", "123456789");
        record.put("amount", "119.99");
        record.put("free", "Free style field_?1|' ^^");

        command.process(record);
        List<Record> records = collectorChild.getRecords();
        
        assertEquals(records.size(),1);
        Record result = records.get(0);
        assertEquals(0, result.get("vgrok_error_fields").size());
    }

    @Test
    public void vgrokSingleWrongField(){
        final MorphlineContext context = new MorphlineContext.Builder().build();
        Collector collectorParent = new Collector();
        Collector collectorChild = new Collector();
        final Command command = new VgrokBuilder().build(config, collectorParent,
                collectorChild, context);

        Record record = new Record();
        record.put("optionalHash", "");
        record.put("mandatoryHash", "");
        record.put("date", "1972-01-01T00:00:00+01:00");
        record.put("id", "123456789");
        record.put("amount", "119.99");
        record.put("free", "Free style field_?1|' ^^");

        command.process(record);
        List<Record> records = collectorChild.getRecords();

        assertEquals(records.size(),1);
        Record result = records.get(0);
        checkFieldValue(result, "vgrok_error_fields", "[mandatoryHash]");
    }

    @Test
    public void vgrokMultipleWrongField(){
        final MorphlineContext context = new MorphlineContext.Builder().build();
        Collector collectorParent = new Collector();
        Collector collectorChild = new Collector();
        final Command command = new VgrokBuilder().build(config, collectorParent,
                collectorChild, context);

        Record record = new Record();
        record.put("optionalHash", "");
        record.put("mandatoryHash", "");
        record.put("date", "1972-01-01X00:00:00+01:00");
        record.put("id", "");
        record.put("amount", "119,99");
        record.put("free", "Free style field_?1|' ^^");

        command.process(record);
        List<Record> records = collectorChild.getRecords();

        assertEquals(records.size(),1);
        Record result = records.get(0);
        checkFieldValue(result, "vgrok_error_fields", "[amount, mandatoryHash, date]");
    }

}
