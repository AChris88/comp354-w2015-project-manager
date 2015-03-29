package util;

import java.util.ArrayList;

import dataAccess.DatabaseManager;
import obj.Project;
import obj.Task;

public class ProjectAnalysisUtil {

	DatabaseManager dm;
	ArrayList<Task> tasks;

	public ProjectAnalysisUtil(Project project) {
		dm = new DatabaseManager();
		tasks = dm.getTasksForProject(project);
	}

	public long getCriticalPath() {
		ArrayList<Long> sequences = new ArrayList<Long>();
		Task task = null;
		long duration = 0L;
		
		for (int i = tasks.size() - 1; i > 0; i--) {
			task = dm.getTaskPrereq(tasks.get(i));
			if (task != null) {
				sequences.add(findTaskChainDuration(task));
			}
		}
		
		for(int i=0; i<sequences.size(); i++){
			if(sequences.get(i) > duration)
				duration = sequences.get(i);
		}
			
		return duration;
	}

	public void pertAnalysis() {

	}

	public void earnedValueAnalysis() {

	}

	private long findTaskChainDuration(Task task) {
		Task prereq = dm.getTaskPrereq(task);
		
		if (prereq != null)
			return findTaskChainDuration(prereq)
					+ (task.getEndDate().getTime() - task
							.getStartDate().getTime());
		else
			return task.getEndDate().getTime()
					- task.getStartDate().getTime();
	}
}