/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2020-04-16 01:34:02 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.center.video;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class reportList_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("<script src=\"");
      out.print(request.getContextPath() );
      out.write("/static/pages/js/reportList.js?v=1.1\" type=\"text/javascript\"></script>\r\n");
      out.write("\r\n");
      out.write("\t<!-- BEGIN PAGE HEADER-->\r\n");
      out.write("\t<!-- BEGIN PAGE BAR -->\r\n");
      out.write("\t<div class=\"page-bar\">\r\n");
      out.write("\t    <ul class=\"page-breadcrumb\">\r\n");
      out.write("\t        <li>\r\n");
      out.write("\t            <span>首页</span>\r\n");
      out.write("\t            <i class=\"fa fa-circle\"></i>\r\n");
      out.write("\t        </li>\r\n");
      out.write("\t        <li>\r\n");
      out.write("\t            <span>举报信息</span>\r\n");
      out.write("\t            <i class=\"fa fa-circle\"></i>\r\n");
      out.write("\t        </li>\r\n");
      out.write("\t        <li>\r\n");
      out.write("\t            <span>举报列表</span>\r\n");
      out.write("\t        </li>\r\n");
      out.write("\t    </ul>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<!-- END PAGE BAR -->\r\n");
      out.write("\t<!-- END PAGE HEADER-->\r\n");
      out.write("        \r\n");
      out.write("    <!-- 列表 jqgrid start -->                \r\n");
      out.write("\t<div class=\"row\">\r\n");
      out.write("\t\r\n");
      out.write("    \t<div class=\"col-md-12\">\r\n");
      out.write("\t\t\t<br/>\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t<div class=\"usersReportsList_wrapper\">  \r\n");
      out.write("\t\t\t    <table id=\"usersReportsList\"></table>  \r\n");
      out.write("\t\t\t    <div id=\"usersReportsListPager\"></div>  \r\n");
      out.write("\t\t\t</div>  \r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<!-- 列表 jqgrid end -->\r\n");
      out.write("\t\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
