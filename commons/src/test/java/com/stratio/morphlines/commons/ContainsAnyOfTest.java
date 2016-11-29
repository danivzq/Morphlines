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


import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Fields;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ContainsAnyOfTest extends BaseTest {

    private static final String ANY_FILE = "/readxml/test.xml";

    @Test
    public void testBasicMultiply() throws IOException {
        final MorphlineContext context = new MorphlineContext.Builder().build();
        Collector collectorParent = new Collector();
        Collector collectorChild = new Collector();
        final Command command = new ContainsAnyOfBuilder().build(parse("/containsanyof/simple.conf")
                        .getConfigList("commands").get(0).getConfig("containsAnyOf"), collectorParent,
                collectorChild, context);

        Record record = new Record();
        record.put("app", "one");
        record.put(Fields.ATTACHMENT_BODY,
                new FileInputStream(new File(getClass().getResource(ANY_FILE).getPath())));

        command.process(record);
        List<Record> records = collectorChild.getRecords();
        Assert.assertEquals(records.size(),1);
    }
}
