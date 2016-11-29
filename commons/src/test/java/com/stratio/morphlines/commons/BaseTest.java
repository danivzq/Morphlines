package com.stratio.morphlines.commons;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import org.apache.commons.io.IOUtils;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by dvazquez on 28/11/16.
 */
public class BaseTest {

    protected Config parse(String file, Config... overrides) throws IOException {
        File tmpFile = File.createTempFile("morphlines_", ".conf");
        IOUtils.copy(getClass().getResourceAsStream(file), new FileOutputStream(tmpFile));
        Config config = new Compiler().parse(tmpFile, overrides);
        config = config.getConfigList("morphlines").get(0);
        Preconditions.checkNotNull(config);
        return config;
    }

    protected void checkFieldValue(Record record, String fieldName, String expectedValue){
        assertEquals(1, record.get(fieldName).size());
        assertEquals(expectedValue, record.get(fieldName).get(0));
    }

}
