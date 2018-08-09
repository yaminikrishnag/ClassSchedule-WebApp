<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="org.yamini.classschedule.SchoolSchedule" %>
<%@ page import="org.yamini.simpledb.DBManage" %>
<%@ page import="java.io.PrintWriter" %>
<HTML>
<HEAD>
<TITLE>Student Schedule</TITLE>
</HEAD>


<BODY>
<FORM action="/ScheduleServlet-web" method="post">
Course Name: <INPUT type="text" name="title" size="35"><BR>
Course Time: 
Sun<INPUT type="checkbox" name="day" value="sun"> 
Mon<INPUT type="checkbox" name="day" value="mon"> 
Tue<INPUT type="checkbox" name="day" value="tue"> 
Wed<INPUT type="checkbox" name="day" value="wed"> 
Thru<INPUT type="checkbox" name="day" value="thru"> 
Fri<INPUT type="checkbox" name="day" value="fri"> 
Sat<INPUT type="checkbox" name="day" value="sat"> 
<SELECT name="starttime">
<OPTION value="8">8:00am</OPTION>
<OPTION value="9">9:00am</OPTION>
<OPTION value="10">10:00am</OPTION>
<OPTION value="11">11:00am</OPTION>
<OPTION value="12">12:00pm</OPTION>
<OPTION value="13">1:00pm</OPTION>
<OPTION value="14">2:00pm</OPTION>
<OPTION value="15">3:00pm</OPTION>
<OPTION value="16">4:00pm</OPTION>
<OPTION value="17">5:00pm</OPTION>
<OPTION value="18">6:00pm</OPTION>
<OPTION value="19">7:00pm</OPTION>
<OPTION value="20">8:00pm</OPTION>
<OPTION value="21">9:00pm</OPTION>
</SELECT> 
to 
<SELECT name="endtime">
<OPTION value="9">9:00am</OPTION>
<OPTION value="10">10:00am</OPTION>
<OPTION value="11">11:00am</OPTION>
<OPTION value="12">12:00pm</OPTION>
<OPTION value="13">1:00pm</OPTION>
<OPTION value="14">2:00pm</OPTION>
<OPTION value="15">3:00pm</OPTION>
<OPTION value="16">4:00pm</OPTION>
<OPTION value="17">5:00pm</OPTION>
<OPTION value="18">6:00pm</OPTION>
<OPTION value="19">7:00pm</OPTION>
<OPTION value="20">8:00pm</OPTION>
<OPTION value="21">9:00pm</OPTION>
<OPTION value="22">10:00pm</OPTION>
</SELECT>
<BR>
<BR>
<INPUT type="submit" name="Submit" value="Add Course">
</FORM>
<TABLE border="1" cellspacing="0">
<TBODY>
<TR>
<TH align="center" valign="middle" width="80"></TH>
<TH align="center" valign="middle" width="100">Sunday</TH>
<TH align="center" valign="middle">Monday</TH>
<TH align="center" valign="middle">Tuesday</TH>
<TH align="center" valign="middle">Wednesday</TH>
<TH align="center" valign="middle">Thursday</TH>
<TH align="center" valign="middle">Friday</TH>
<TH align="center" valign="middle">Saturday</TH>
</TR>
<c:forEach begin="8" end="21" step="1" var="time">
<TR>
<TD align="center" valign="middle" width="80">
<c:choose>
<c:when test="${time == 12}">
<c:out value="${time}" />:00pm
</c:when>
<c:when test="${time > 12}">
<c:out value="${time - 12}" />:00pm
</c:when>
<c:otherwise>
<c:out value="${time}" />:00am
</c:otherwise>
</c:choose></TD>
<c:forEach begin="0" end="6" step="1" var="day">
<TD align="center" valign="middle" width="100">
<c:forEach items="${schoolschedule.classes}" var="clazz">
<c:if test="${clazz.startTime <= time 
&& clazz.endTime > time 
&& clazz.day == day}">
<c:out value="${clazz.title}" />
</c:if>
</c:forEach>
</TD>
</c:forEach>
</TR>
</c:forEach>
</TBODY>
</TABLE>
</BODY>
<%
 // Populate the schedule from SimpleDB
 SchoolSchedule mySchedule = new SchoolSchedule();
 PrintWriter outputPrinter = new PrintWriter(out);
 DBManage myDB = new DBManage(outputPrinter);
 mySchedule = myDB.loadSchedule();

 // Hack: Save the schedule to session, so that existing tutorial code
 // (see below) can load it back out and display it
 request.getSession().setAttribute("schoolschedule", mySchedule);
%>
</HTML>