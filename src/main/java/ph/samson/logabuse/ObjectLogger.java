/*
 * Copyright 2012 OSRP, LLC.
 */
package ph.samson.logabuse;

import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;

/**
 * Utility class for logging out stuff to your Logger of choice. Useful for
 * inspecting objects during development time. All calls return immediately if
 * TRACE level logging is not enabled on the given Logger.
 *
 * @author Edward Samson <edward@samson.ph>
 */
public final class ObjectLogger {

    private static final DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
    private static final String I1 = "\n  ";
    private static final String I2 = "\n    ";
    private static final String I3 = "\n      ";
    private static final String I4 = "\n        ";

    private ObjectLogger() {
    }

    /**
     * Generate a TRACE level log of the state of the given ServletRequest.
     * 
     * @param log
     * @param request 
     */
    public static void traceLog(Logger log, ServletRequest request) {
        if (!log.isTraceEnabled()) {
            return;
        }

        try {
            StringBuilder trace = new StringBuilder("ServletRequest = {");

            trace.append(I1).append("remoteAddr = ").append(request.getRemoteAddr());

            trace.append(I1).append("parameters = {");
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                trace.append(I2).append(entry.getKey()).append(" = [");
                trace.append(StringUtils.join(entry.getValue(), ", "));
                trace.append("]");
            }
            trace.append(I1).append("}");

            if (request instanceof HttpServletRequest) {
                HttpServletRequest hsr = (HttpServletRequest) request;
                trace.append(I1).append("HttpServletRequest = {");

                trace.append(I2).append("requestURI = ").append(hsr.getRequestURI());
                trace.append(I2).append("queryString = ").append(hsr.getQueryString());
                trace.append(I2).append("method = ").append(hsr.getMethod());
                trace.append(I2).append("requestedSessionId = ").append(hsr.getRequestedSessionId());
                trace.append(I2).append("isRequestedSessionIdValid = ").
                        append(hsr.isRequestedSessionIdValid());

                HttpSession session = hsr.getSession(false);
                trace.append(I2).append("session = {");
                if (session == null) {
                    trace.append(I3).append("NONE");
                } else {
                    trace.append(I3).append("id = ").append(session.getId());
                    trace.append(I3).append("creationTime = ").
                            append(dtf.print(session.getCreationTime()));
                    trace.append(I3).append("lastAccessedTime = ").
                            append(dtf.print(session.getLastAccessedTime()));
                    trace.append(I3).append("maxInactiveInterval = ").
                            append(session.getMaxInactiveInterval());
                    trace.append(I3).append("isNew = ").append(session.isNew());

                    Enumeration<String> attributeNames = session.getAttributeNames();
                    trace.append(I3).append("attributes").append(" = {");
                    while (attributeNames.hasMoreElements()) {
                        String name = attributeNames.nextElement();
                        trace.append(I4).append(name).append(" = ").append(session.getAttribute(name));
                    }
                    trace.append(I3).append("}");
                }
                trace.append(I2).append("}");

                Cookie[] cookies = hsr.getCookies();
                trace.append(I2).append("cookies = {");
                if (cookies == null) {
                    trace.append(I3).append("NONE");
                } else {
                    for (Cookie cookie : cookies) {
                        trace.append(I3).append(cookie.getName()).append(" = {");
                        trace.append(I4).append("comment = ").append(cookie.getComment());
                        trace.append(I4).append("domain = ").append(cookie.getDomain());
                        trace.append(I4).append("maxAge = ").append(cookie.getMaxAge());
                        trace.append(I4).append("path = ").append(cookie.getPath());
                        trace.append(I4).append("secure = ").append(cookie.getSecure());
                        trace.append(I4).append("value = ").append(cookie.getValue());
                        trace.append(I4).append("httpOnly = ").append(cookie.isHttpOnly());
                        trace.append(I3).append("}");
                    }
                }
                trace.append(I2).append("}");

                Enumeration<String> headerNames = hsr.getHeaderNames();
                trace.append(I2).append("headers = {");
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    trace.append(I3).append(name).append(" = [");
                    Enumeration<String> headers = hsr.getHeaders(name);
                    while (headers.hasMoreElements()) {
                        trace.append(I4).append(headers.nextElement());
                    }
                    trace.append(I3).append("]");
                }
                trace.append(I2).append("}");

                trace.append(I2).append("requestURL = ").append(hsr.getRequestURL());
                trace.append(I2).append("remoteUser = ").append(hsr.getRemoteUser());
                trace.append(I2).append("authType = ").append(hsr.getAuthType());
                trace.append(I2).append("contextPath = ").append(hsr.getContextPath());
                trace.append(I2).append("servletPath = ").append(hsr.getServletPath());
                trace.append(I2).append("pathInfo = ").append(hsr.getPathInfo());
                trace.append(I2).append("pathTranslated = ").append(hsr.getPathTranslated());
                trace.append(I2).append("isRequestedSessionIdFromCookie = ").
                        append(hsr.isRequestedSessionIdFromCookie());
                trace.append(I2).append("isRequestedSessionIdFromURL = ").
                        append(hsr.isRequestedSessionIdFromURL());

                trace.append(I1).append("}");
            }

            trace.append(I1).append("protocol = ").append(request.getProtocol());
            trace.append(I1).append("remoteHost = ").append(request.getRemoteHost());
            trace.append(I1).append("remotePort = ").append(request.getRemotePort());
            trace.append(I1).append("scheme = ").append(request.getScheme());
            trace.append(I1).append("serverName = ").append(request.getServerName());
            trace.append(I1).append("serverPort = ").append(request.getServerPort());
            trace.append(I1).append("secure = ").append(request.isSecure());

            trace.append(I1).append("characterEncoding = ").append(request.getCharacterEncoding());
            trace.append(I1).append("contentLength = ").append(request.getContentLength());
            trace.append(I1).append("contentType = ").append(request.getContentType());
            trace.append(I1).append("localAddr = ").append(request.getLocalAddr());
            trace.append(I1).append("localPort = ").append(request.getLocalPort());

            Enumeration<String> attributeNames = request.getAttributeNames();
            trace.append(I1).append("attributes = {");
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                Object value = request.getAttribute(name);
                trace.append(I2).append(name).append(" = ").append(value);
            }
            trace.append(I1).append("}");

            trace.append("\n}");

            log.trace(trace.toString());
        } catch (Exception ex) {
            log.trace("error in traceLog({})", request, ex);
        }
    }
}
