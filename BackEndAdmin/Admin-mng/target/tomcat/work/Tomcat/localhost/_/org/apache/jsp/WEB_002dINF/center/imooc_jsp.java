/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2020-04-16 02:28:34 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.center;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class imooc_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("        \n");
      out.write("<style>\n");
      out.write("\n");
      out.write(".desc {\n");
      out.write("\tline-height:18px;\n");
      out.write("\ttext-indent:2em;\n");
      out.write("}\n");
      out.write("\n");
      out.write("</style>\n");
      out.write("        \n");
      out.write("\t<div class=\"page-bar\">\n");
      out.write("\t    <ul class=\"page-breadcrumb\">\n");
      out.write("\t        <li>\n");
      out.write("\t            <a href=\"index.html\">首页</a>\n");
      out.write("\t        </li>\n");
      out.write("\t    </ul>\n");
      out.write("\t</div>\n");
      out.write("\t\n");
      out.write("\t<h1 class=\"page-title\"> 短视频后台管理系统 </h1>\n");
      out.write("                        \n");
      out.write("\t<div class=\"clearfix\"></div>\n");
      out.write("        \n");
      out.write("\t<div class=\"row\">\n");
      out.write("\t    <div class=\"col-lg-6 col-xs-12 col-sm-12\">\n");
      out.write("\t        \n");
      out.write("\t        <div class=\"portlet light portlet-fit bordered\">\n");
      out.write("\t            <div class=\"portlet-title\">\n");
      out.write("\t                <div class=\"caption\">\n");
      out.write("\t                    <i class=\"icon-microphone font-dark hide\"></i>\n");
      out.write("\t                    <span class=\"caption-subject bold font-dark uppercase\"> 平台技术简介</span>\n");
      out.write("\t                    <span class=\"caption-helper\">小程序后台接口  + 短视频后台管理</span>\n");
      out.write("\t                </div>\n");
      out.write("\t            </div>\n");
      out.write("\t            <div class=\"portlet-body\">\n");
      out.write("\t                <div class=\"row\">\n");
      out.write("\t                    <div class=\"col-md-12\">\n");
      out.write("\t                        <div class=\"mt-widget-3\" style=\"padding-top: 15px;\">\n");
      out.write("\t                        \t<p class=\"desc\"><font style=\"font-size: 16px;\"><b>小程序api - 技术选型：</b></font></p>\n");
      out.write("\t                        \t<p class=\"desc\">1、核心框架：Spring Framework 4.3.14.RELEASE</p>\n");
      out.write("\t                        \t<p class=\"desc\">2、SpringBoot：1.5.10.RELEASE</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">3、持久层框架：MyBatis 3.4.5 + pagehelper 5.1.2</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">4、MariaDB 10.2.6</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">5、数据库连接池：阿里巴巴 Druid 1.1.0</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">6、zookeeper：3.4.11</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">7、spring-data-redis：1.8.7.RELEASE</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">8、swagger2：2.4.0</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">9、FFmpeg：2.0.1.1</p>\n");
      out.write("\t                        </div>\n");
      out.write("\t                    </div>\n");
      out.write("\t                </div>\n");
      out.write("\t                \n");
      out.write("\t                <div class=\"row\" style=\"margin-top: 15px;\">\n");
      out.write("\t                    <div class=\"col-md-12\">\n");
      out.write("\t                        <div class=\"mt-widget-3\" style=\"padding-top: 15px;\">\n");
      out.write("\t                        \t<p class=\"desc\"><font style=\"font-size: 16px;\"><b>短视频后台 - 技术选型：</b></font></p>\n");
      out.write("\t                        \t<p class=\"desc\">1、核心框架：Spring Framework 4.3.8.RELEASE</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">2、持久层框架：MyBatis 3.2.8 + pagehelper 4.1.3</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">3、MariaDB 10.2.6</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">4、数据库连接池：阿里巴巴 Druid 1.1.0</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">5、jackson：2.7.4</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">6、slf4j：1.7.21</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">7、zookeeper：3.4.11</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">8、前端框架：Bootstrap + Jquery</p>\n");
      out.write("\t\t\t\t\t\t\t\t<p class=\"desc\">9、前端分页组件：jqGrid</p>\n");
      out.write("\t                        </div>\n");
      out.write("\t                    </div>\n");
      out.write("\t                </div>\n");
      out.write("\t                \n");
      out.write("<!-- \t                <div class=\"row\" style=\"margin-top: 15px;\"> -->\n");
      out.write("<!-- \t                    <div class=\"col-md-12\"> -->\n");
      out.write("<!-- \t                        <div class=\"mt-widget-3\" style=\"padding-top: 15px;\"> -->\n");
      out.write("<!-- \t                        \t彩蛋：分布式集群架构的演变 -->\n");
      out.write("<!-- \t                        </div> -->\n");
      out.write("<!-- \t                    </div> -->\n");
      out.write("<!-- \t                </div> -->\n");
      out.write("\t            </div>\n");
      out.write("\t        </div>\n");
      out.write("\t        \n");
      out.write("\t    </div>\n");
      out.write("\t\n");
      out.write("\t</div>\n");
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
