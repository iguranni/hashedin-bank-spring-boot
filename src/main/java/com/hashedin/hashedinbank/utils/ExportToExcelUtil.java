package com.hashedin.hashedinbank.utils;

import lombok.extern.slf4j.Slf4j;

import static com.hashedin.hashedinbank.utils.CommonUtils.currentDateTime;

@Slf4j
public class ExportToExcelUtil {
    private ExportToExcelUtil() {
    }

    public static String getWorkBookFileName(String prefixTitle) {
        return prefixTitle + "_" + currentDateTime() + ".xlsx";
    }

    public static String getWorkBookSheetName(String prefixTitle) {
        return prefixTitle;
    }
}
