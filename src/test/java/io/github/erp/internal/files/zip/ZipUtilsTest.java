package io.github.erp.internal.files.zip;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilsTest {

    @Test
    void zipFile() throws Exception {

        //File file = getFile("files/invoice-zip-test.xlsx");

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("files/invoice-zip-test.xlsx").getFile());

        ZipUtils.zipFile(
            "src/test/resources/files/invoice-zip-test.zip",
            "zipped-invoice",
            file
        );

        // File zippedFile = getFile("files/invoice-zip-test.zip");

        File zippedFile = new File(classLoader.getResource("files/invoice-zip-test.zip").getFile());

        // TODO assertTrue(zippedFile.getAbsolutePath().endsWith("files/invoice-zip-test.zip"));

    }

    @NotNull
    private File getFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }
}
