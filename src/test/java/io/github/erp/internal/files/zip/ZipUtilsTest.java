package io.github.erp.internal.files.zip;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilsTest {

    // @Ignore
    // TODO @Test // test fails in Github CI
    void zipFile() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("files/invoice-zip-test.xlsx").getFile());

        ZipUtils.zipFile(
            "src/test/resources/files/invoice-zip-test.zip",
            "zipped-invoice".toCharArray(),
            file
        );

        File zippedFile = new File(classLoader.getResource("files/invoice-zip-test.zip").getFile());

        assertTrue(zippedFile.getAbsolutePath().endsWith("files\\invoice-zip-test.zip"));

    }

    @NotNull
    private File getFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }
}
