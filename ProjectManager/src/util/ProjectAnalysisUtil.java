package util;

import java.util.ArrayList;

import dataAccess.DatabaseManager;
import obj.Project;
import obj.Task;

/**
 * 
 * @author Christian Allard
 * 
 *         Utility used to perform project analysis such as critical path
 *         analysis, PERT analysis, and earned value analysis.
 */

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

		int day = 1000 * 60 * 60 * 24;

		for (Task task : tasks) {
			taskTitle = task.getName();
			prereqs = dm.getTaskRequirements(task);

			if (prereqs.size() > 0) {
				for (Task req : prereqs) {
					taskReqTitles = taskReqTitles + req.getName() + ", ";
				}
			}
			
			shortest = (task.getProjectedEndDate().getTime() - task
					.getProjectedStartDate().getTime()) / day;
			likely = (shortest * likelyMod) / day;
			worst = (shortest * pessimisticMod) / day;
			expected = ((shortest + (4 * likely) + worst) / 6) / day;
			deviation = ((worst - likely) / 6) / day;

			String[] record = {
					taskTitle,
					taskReqTitles,
					"" + shortest + (shortest == 1 ? " day" : " days"),
					("" + likely).substring(0, ("" + likely).indexOf('.') + 4) + (likely == 1 ? " day" : " days"),
					("" + worst).substring(0, ("" + worst).indexOf('.') + 4) + (worst == 1 ? " day" : " days"),
					("" + expected).substring(0,
							("" + expected).indexOf('.') + 4) + (expected == 1 ? " day" : " days"),
					("" + deviation).substring(0,
							("" + deviation).indexOf('.') + 4) };

			pert.add(record);
		}
		return pert;
	}

	public int[] earnedValueAnalysis() {
		int[] values = new int[2];
		int earnedValue = 0;
		Task task = null;
		int total = 0;
		for (int i = 0; i < tasks.size(); i++) {
			task = tasks.get(i);
			if (task.getEndDate() != null) {
				earnedValue += task.getValue();
			}
			total += task.getValue();
		}

		values[0] = earnedValue;
		values[1] = total;

		return values;
	}

	private long findTaskChainDuration(Task task) {

		long duration = -1L;

		if (task.getStartDate() != null) {
			if (task.getEndDate() != null) {
				duration = task.getEndDate().getTime()
						- task.getStartDate().getTime();
			} else if (task.getProjectedEndDate() != null) {
				duration = task.getProjectedEndDate().getTime()
						- task.getStartDate().getTime();
			}
		} else if (task.getProjectedStartDate() != null) {
			if (task.getEndDate() != null) {
				duration = task.getEndDate().getTime()
						- task.getProjectedStartDate().getTime();
			} else if (task.getProjectedEndDate() != null) {
				duration = task.getProjectedEndDate().getTime()
						- task.getProjectedStartDate().getTime();
			}
		}

		if (duration != -1L) {
			ArrayList<Task> prereqs = dm.getTaskRequirements(task);
			ArrayList<Long> sequences = new ArrayList<Long>();
			long chainDuration = 0L;

			for (Task t : prereqs) {
				sequences.add(findTaskChainDuration(t));
			}

			for (int i = 0; i < sequences.size(); i++) {
				if (sequences.get(i) > chainDuration)
					chainDuration = sequences.get(i);
			}

			if (chainDuration != -1) {
				duration += chainDuration;
			} else {
				duration = -1L;
			}
		}
		return duration;
	}
}