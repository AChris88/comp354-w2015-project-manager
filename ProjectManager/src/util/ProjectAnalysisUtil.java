package util;

import java.util.ArrayList;

import dataAccess.DatabaseManager;
import obj.Project;
import obj.Task;

public class ProjectAnalysisUtil {

	DatabaseManager dm;
	ArrayList<Task> tasks;
	Project project;

	public ProjectAnalysisUtil(Project project) {
		dm = new DatabaseManager();
		tasks = dm.getTasksForProject(project);
		this.project = project;
	}

	public long getCriticalPath() {
		ArrayList<Long> sequences = new ArrayList<Long>();
		long duration = 0L;
		ArrayList<Task> bottomLevelTasks = dm.getBottomLevelTasks(project);

		for (Task t : bottomLevelTasks) {
			sequences.add(findTaskChainDuration(t));
		}

		for (int i = 0; i < sequences.size(); i++) {
			if (sequences.get(i) > duration)
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
		for (int i = 0; i < tasks.size(); i++) {
			task = tasks.get(i);
			if (task.getEndDate() != null) {
				earnedValue += task.getValue();
			}
		}

		return earnedValue;
	}

	private long findTaskChainDuration(Task task) {
		ArrayList<Task> prereqs = dm.getTaskRequirements(task);

		if (prereqs.size() == 0)
			return task.getEndDate().getTime() - task.getStartDate().getTime();
		else {
			ArrayList<Long> sequences = new ArrayList<Long>();
			long duration = 0L;

			for (Task t : prereqs) {
				sequences.add(findTaskChainDuration(t));
			}

			for (int i = 0; i < sequences.size(); i++) {
				if (sequences.get(i) > duration)
					duration = sequences.get(i);
			}
			return duration + task.getEndDate().getTime()
					- task.getStartDate().getTime();
		}
	}
}