package com.hashedin.hashedinbank.services;

public interface JobAuditErrorService {

    void parseSuccess(String status, String comment);

    void parseFailed(String errorDescription, String errorStackTrace);

    void saveJobErrorInfo(String errorDescription, String errorStackTrace);

}
