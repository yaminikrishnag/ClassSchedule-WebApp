package org.yamini.classschedule;
import java.util.ArrayList;
import java.util.List;

public class SchoolSchedule {
	private List<SchoolClass> classes = new ArrayList<SchoolClass>();
	public List<SchoolClass> getClasses() {
		return classes;
	}
	public void addClass(SchoolClass schoolClass)
	{
	   classes.add(schoolClass);
	}
}
yamini-Scheduleryamini-Scheduler