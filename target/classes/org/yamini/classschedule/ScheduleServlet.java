package org.yamini.classschedule;
import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScheduleServlet extends HttpServlet implements Servlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String title = req.getParameter("title");
		int starttime = Integer.parseInt(req.getParameter("starttime"));
		int endtime = Integer.parseInt(req.getParameter("endtime"));
		String[] days = req.getParameterValues("day");
		SchoolSchedule schedule = (SchoolSchedule)req.getSession(true).getAttribute("SchoolSchedule");
		if(schedule == null)
		{
		   schedule = new SchoolSchedule();
		}if(days != null)
		{
			   for(int i = 0; i < days.length; i++)
			   {
			      String dayString = days[i];
			      int day;
			      if(dayString.equalsIgnoreCase("SUN")) day = 0;
			      else if(dayString.equalsIgnoreCase("MON")) day = 1;
			      else if(dayString.equalsIgnoreCase("TUE")) day = 2;
			      else if(dayString.equalsIgnoreCase("WED")) day = 3;
			      else if(dayString.equalsIgnoreCase("THRU")) day = 4;
			      else if(dayString.equalsIgnoreCase("FRI")) day = 5;
			      else day = 6;

			      PrintWriter out = resp.getWriter();
			      DBManage myDB = new DBManage(out);
			      myDB.storeClass(title, starttime, endtime, day);
			   }
			}
		//req.getSession().setAttribute("SchoolSchedule", schedule);
		getServletContext().getRequestDispatcher("/Schedule.jsp").forward(req, resp);
	}


}
