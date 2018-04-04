package com.tsystems.timetable.dispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//Create Dispatcher which forwards request to correct xhtml page
public class Dispatcher {

    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageBase = "/WEB-INF/jsf/";
        String pagePath;

        String pageName = "index.xhtml";

        pagePath = pageBase + pageName;

        request.setAttribute("station", request.getPathInfo().substring(1));

        //forward to page inside WEB-INF/jsf
        request.getServletContext().getRequestDispatcher(pagePath).
                forward(request, response);
    }

}
