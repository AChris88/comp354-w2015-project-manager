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
		double optimisticMod = 1.25;
		double pessimisticMod = 1.5;
		
	}

	public int earnedValueAnalysis() {
		int earnedValue = 0;
		Task task = null;
		for(int i = 0; i<tasks.size(); i++){
			task = tasks.get(i);
			if(task.getEndDate()!= null){
				earnedValue += task.getValue();
			}
		}
		
		return earnedValue;
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