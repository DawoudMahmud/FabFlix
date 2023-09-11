package com.gitcodings.stack.core.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class ServiceRequestLogger extends AbstractRequestLoggingFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(ServiceRequestLogger.class);

    public ServiceRequestLogger()
    {
        this.setIncludeClientInfo(false);
        this.setIncludeQueryString(true);
        this.setIncludePayload(false);
        this.setBeforeMessagePrefix("Incoming Request: [");
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message)
    {
        LOG.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message)
    {

    }
}
