package org.sonatype.mavenbook.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
//@WebServlet(description = "Locate Streams application's streams port number and IP ", urlPatterns = { "/TestTwo" })
public class TestTwo extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	out.println( "Your are at test." );
        out.flush();
        out.close();
    }
}
/*

package org.sonatype.mavenbook.web;

//import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet(description = "Locate Streams application's streams port number and IP ", urlPatterns = { "/TopoDisplay" })
public class TestTwo extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {
       	PrintWriter out = response.getWriter();
	out.println( "TestExecuted Executed" );
        out.flush();
        out.close();
    }
}
*/
