/**
 * Created by Samuel on 3/8/2015.
 */

import application.ProjectManager;
import dataAccess.DatabaseManager;
import obj.Project;
import obj.User;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.*;

import static org.junit.Assert.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

// Other GANTT-related libraries
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class JFreeChartTest {

    private DatabaseManager _dbm;
    private Project _project;
    private JFrame _frame;

    @Before
    public void testSetup() {

        File testDbFile = new File("itTest.db");
        if (testDbFile.exists()) {
            testDbFile.delete();
        }

        _dbm = new DatabaseManager("itTest.db");

        _project = new Project(0, "Integration test project", new Date(), new Date(), new Date());

        User pMan = new User(0, "PManager", "Manager", "mananger", 1);

        _dbm.insertUser(pMan, "pwd");
        _dbm.insertProject(_project, pMan);

        Calendar test = Calendar.getInstance();

        test.set(2015, Calendar.MARCH, 8);
        Date test2 = test.getTime();
        test.set(Calendar.MONTH, Calendar.MAY);
        Date test3 = test.getTime();


//
//                Runnable r = new Runnable() {
//            public void run() {
//                _frame = new JFrame("Charts");
//
//                _frame.setSize(600, 400);
//                _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                _frame.setVisible(true);
//            }
//        };
//
//        SwingUtilities.invokeLater(r);
    }



    @Test(expected = IllegalArgumentException.class)
    public void ErrorHandlingTest() {
        obj.Task t1 = new obj.Task(0, _project.getId(), "Do the first thing", date(9, Calendar.MARCH, 2015), date(9, Calendar.MARCH, 2015), date(1, Calendar.MARCH, 2015), date(10, Calendar.MARCH, 2015), 2);
        assumeTrue(_dbm.insertTask(t1));

        ArrayList<obj.Task> tasks = _dbm.getTasksForProject(_project);
        assumeTrue(tasks.size() == 1);

        IntervalCategoryDataset data = createDataset(tasks);

    }

    @Test
    public void InterpretationTest() throws InterruptedException {


        obj.Task t1 = new obj.Task(0, _project.getId(), "Do the first thing", date(9, Calendar.MARCH, 2015), date(9, Calendar.MARCH, 2015), date(17, Calendar.MARCH, 2015), date(10, Calendar.MARCH, 2015), 2);
        assumeTrue(_dbm.insertTask(t1));

        obj.Task t2 = new obj.Task(0, _project.getId(), "Proceed to test this", date(16, Calendar.MARCH, 2015), date(9, Calendar.MARCH, 2015), date(24, Calendar.MARCH, 2015), date(10, Calendar.MARCH, 2015), 2);
        assumeTrue(_dbm.insertTask(t2));

        obj.Task t3 = new obj.Task(0, _project.getId(), "Integration tests", date(23, Calendar.MARCH, 2015), date(9, Calendar.MARCH, 2015), date(31, Calendar.MARCH, 2015), date(10, Calendar.MARCH, 2015), 2);
        assumeTrue(_dbm.insertTask(t3));

        obj.Task t4 = new obj.Task(0, _project.getId(), "Unit tests", date(9, Calendar.MARCH, 2015), date(9, Calendar.MARCH, 2015), date(30, Calendar.MARCH, 2015), date(10, Calendar.MARCH, 2015), 4);
        assumeTrue(_dbm.insertTask(t4));

        obj.Task t5 = new obj.Task(0, _project.getId(), "Documentation", date(30, Calendar.MARCH, 2015), date(1, Calendar.MARCH, 2015), date(10, Calendar.APRIL, 2015), date(1, Calendar.APRIL, 2015), 5);
        assumeTrue(_dbm.insertTask(t5));

        ArrayList<obj.Task> tasks = _dbm.getTasksForProject(_project);
        assumeTrue(tasks.size() == 5);

        IntervalCategoryDataset data = createDataset(tasks);

        TaskSeries ts = ((TaskSeriesCollection) data).getSeries(0);

        assertEquals(5, ts.getItemCount());

        JFreeChart chart = createChart(data);

        assertNotNull(chart);

        ChartPanel cp = new ChartPanel(chart);

//        _frame.getContentPane().add(cp);
//        _frame.setVisible(true);

        JComponent[] comp = new JComponent[]{new JLabel("Is this chart the expected result?"), cp};
        int res = JOptionPane.showConfirmDialog(null, comp, "Integration test : Interpretation of Data", JOptionPane.OK_OPTION);

        assertEquals("The user should agree on the correctness of the chart tested", 0, res);


    }


    public static IntervalCategoryDataset createDataset(ArrayList<obj.Task> tasks) {

        final TaskSeries s1 = new TaskSeries("Scheduled");
//        final TaskSeries s2 = new TaskSeries("Actual");

        for (obj.Task t : tasks) {
            s1.add(new Task(t.getName(),
                    new SimpleTimePeriod(t.getProjectedStartDate(), t.getProjectedEndDate())));
        }

//        for (obj.Task t : tasks) {
//            s2.add(new Task(t.getName(),
//                    new SimpleTimePeriod(t.getStartDate(), t.getEndDate())));
//        }


        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
//        collection.add(s2);

        return collection;
    }

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day   the date.
     * @param month the month.
     * @param year  the year.
     * @return a date.
     */
    private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }

    /**
     * Creates a chart.
     *
     * @param dataset the dataset.
     * @return The chart.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
                _project.getName() + " Tasks",  // chart title
                "Task",              // domain axis label
                "Date",              // range axis label
                dataset,             // data
                true,                // include legend
                true,                // tooltips
                true                // urls
        );
//        chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
        return chart;
    }

}
