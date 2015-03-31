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

	public ArrayList<String[]> pertAnalysis() {
		ArrayList<String[]> pert = new ArrayList<String[]>();

		double likelyMod = 1.25;
		double pessimisticMod = 1.5;

		long shortest = 0;
		double likely = 0.0;
		double worst = 0.0;
		double expected = 0.0;
		double deviation = 0.0;

		String taskTitle = "";
		String taskReqTitles = "";
		ArrayList<Task> prereqs = new ArrayList<Task>();

		for (Task task : tasks) {
			taskTitle = task.getName();
			prereqs = dm.getTaskRequirements(task);

			if (prereqs.size() > 0) {
				for (Task req : prereqs) {
					taskReqTitles = taskReqTitles + req.getName() + ", ";
				}
				// remove last comma
				taskReqTitles = taskReqTitles.substring(0,
						taskReqTitles.length() - 2);
			}

			shortest = task.getProjectedEndDate().getTime()
					- task.getProjectedStartDate().getTime();
			likely = shortest * likelyMod;
			worst = shortest * pessimisticMod;
			expected = (shortest + (4 * likely) + worst) / 6;
			deviation = (worst - likely) / 6;

			String[] record = { taskTitle, taskReqTitles, "" + shortest,
					"" + likely, "" + worst, "" + expected, "" + deviation };

			pert.add(record);
		}
		return pert;
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