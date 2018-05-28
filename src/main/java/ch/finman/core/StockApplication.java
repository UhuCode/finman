/**
 * 
 */
package ch.finman.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.finman.model.StockEntry;
import ch.finman.model.logic.AssetManager;
import ch.finman.model.logic.StockEntityManager;
import ch.finman.util.LogUtil;



/**
 * @author kohler
 *
 */
public class StockApplication {
	
	public static JFrame frame;
	private static LogUtil logger = LogUtil.getLogger(StockApplication.class);
	
	private static final String DELIM_CHAR_SEMICOLON = ";";
	private static final String DELIM_CHAR_COMMA = ",";
	
	StockItem ubs = new StockItem("UBS", new BigDecimal(0));
	StockItem csgn = new StockItem("CSGN.VX", new BigDecimal(0));
	StockItem cs = new StockItem("CS", new BigDecimal(0));
	StockItem nes = new StockItem("NESN.VX", new BigDecimal(0));
	StockItem gf = new StockItem("GFIN.F", new BigDecimal(0));

    private String version = "V1.0";
    private String title = "Stock Application";

	private JPanel centerPane;
	private WindowMenu menuBar;	
	private StockManager stockManager;
	private DataTablePanel stockTable;
	private ChartPanel chartPanel;
	//private StockTableModel model;
	private StockSeriesTableModel model;
	private JTable dataTable;
	private TimeSeriesCollection dataSeries;
	private OHLCSeriesCollection ohlcSeriesCollection;
	private ProgressInfo progressInfo;
	private SeriesFilterPanel filterPanel;
	private Logger log;
	private AssetManager assetManager;
	private StockEntityManager stockEntityManager;
	
	public StockApplication() {
		createGUI();
		initialize();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StockApplication();

	}


    private void createGUI() {
    	frame = new JFrame(title + " " + version);
        frame.setResizable(true);
        
        frame.setSize(1600, 800);
        frame.setLocation(50, 50);
		
		// add WindowListener
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
				System.exit(0);     
		    }			
		});
		
		// get pane from frame
		Container pane = frame.getContentPane();

		// create MenuBar
		menuBar = new WindowMenu();
		filterPanel = new SeriesFilterPanel();
		stockTable = new DataTablePanel();
		chartPanel = new ChartPanel(null);
		progressInfo = new ProgressInfo();
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(filterPanel, BorderLayout.NORTH);
		topPanel.add(stockTable, BorderLayout.CENTER);
		
		centerPane = new JPanel();
		centerPane.setLayout(new GridLayout(2, 1));
		centerPane.add(topPanel);
		centerPane.add(chartPanel);
		
        pane.add(menuBar, BorderLayout.NORTH);
        pane.add(centerPane, BorderLayout.CENTER);
        pane.add(progressInfo, BorderLayout.SOUTH);
        
        frame.setVisible(true);
        
        
    }
    
	private void initialize() {
		log = LoggerFactory.getLogger(StockApplication.class);
		log.debug("initialize");
		stockManager = new StockManager(progressInfo);
		dataSeries = new TimeSeriesCollection();
		ohlcSeriesCollection = new OHLCSeriesCollection();
		assetManager = new AssetManager();
		stockEntityManager = new StockEntityManager();
	}
	
  	public void setupDataTable() {
  		SetupDataThread sdt = new SetupDataThread();
  		sdt.start();
	}

  	public void refreshStockData() {
  		RefreshDataThread rdt = new RefreshDataThread();
  		rdt.start();
	}

	public ProgressInfo getProgressInfo() {
		return progressInfo;
	}

	protected void readFile(String datafile) {
		File f = new File(datafile);
		int cnt = 0;
		if (f.exists()) { 
			logger.sayOut("Parsing File: " + f);
			Scanner ls;
			try {
				ls = new Scanner(f);
				String line;
				while((ls.hasNextLine())) {
					line = ls.nextLine();
					if(line.trim().isEmpty()) continue;
					String[] elements = line.split("[" + DELIM_CHAR_SEMICOLON + DELIM_CHAR_COMMA + "]");
					StockItem si = StockItem.create("default", elements);
					logger.sayOut("Reading Line[" + cnt + "]: " + line);
					cnt++;
					stockManager.addStockItem(si);
					//stockManager.addStockItemToPortfolio("default", si);
				}
				ls.close();
				logger.sayOut("Read a total of " + (cnt) + " records.");
				setupDataTable();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.sayOut("File not found: " + datafile);
		}
	}

	protected void readData() {
		int cnt = 0;
		logger.sayOut("Reading from Database ");
		List<StockEntry> stocks = stockEntityManager.getStockEntries();
		for(StockEntry se : stocks) {
			StockItem si = StockItem.create(se.getSymbol());
			stockManager.addStockItem(si);
			cnt++;
		}
		logger.sayOut("Read a total of " + cnt + " records.");
		setupDataTable();
	}

	protected void importFile(String datafile) {
		File f = new File(datafile);
		int cnt = 0;
		if (f.exists()) { 
			logger.sayOut("Parsing File: " + f);
			Scanner ls;
			try {
				ls = new Scanner(f);
				String line;
				while((ls.hasNextLine())) {
					line = ls.nextLine();
					if(line.trim().isEmpty()) continue;
					String[] elements = line.split("[" + DELIM_CHAR_SEMICOLON + DELIM_CHAR_COMMA + "]");
					StockItem si = StockItem.create("default", elements);
					logger.sayOut("Reading Line[" + cnt + "]: " + line);
					cnt++;
					stockEntityManager.importStock(si.getStockSymbol());
					//assetManager.importAsset(si.getStockSymbol(), "DP", si.getStockAmount(), si.getPricePaid());
				}
				ls.close();
				logger.sayOut("Imported " + (cnt) + " records.");
				refreshStockData();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.sayOut("File not found: " + datafile);
		}
	}

	private JFreeChart createTimeSeriesChart(XYDataset dataset, String title, String yAxisLabel) {

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title,  // title
				"Date",             // x-axis label
				yAxisLabel,   // y-axis label
				dataset,            // data
				true,               // create legend?
				true,               // generate tooltips?
				false               // generate URLs?
				);

		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();

		final ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(0.80);
		rangeAxis.setLowerMargin(0.80);

		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setDefaultShapesVisible(true);
			renderer.setDefaultShapesFilled(true);
			renderer.setDrawSeriesLineAsPath(true);
		}

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy kk:mm", Locale.ENGLISH));

		return chart;

	}

	private JFreeChart createOHLCSeriesChart(OHLCDataset dataset, String title, String yAxisLabel) {

		JFreeChart chart = ChartFactory.createHighLowChart(
				title,  // title
				"Date",             // x-axis label
				yAxisLabel,   // y-axis label
				dataset,            // data
				true             // create legend?
				);

		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();

//		final ValueAxis rangeAxis = plot.getRangeAxis();
//		rangeAxis.setUpperMargin(0.80);
//		rangeAxis.setLowerMargin(0.80);
//
//		plot.setBackgroundPaint(Color.lightGray);
//		plot.setDomainGridlinePaint(Color.white);
//		plot.setRangeGridlinePaint(Color.white);
//		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
//		plot.setDomainCrosshairVisible(true);
//		plot.setRangeCrosshairVisible(true);
//
//		XYItemRenderer r = plot.getRenderer();
//		if (r instanceof XYLineAndShapeRenderer) {
//			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
//			renderer.setBaseShapesVisible(true);
//			renderer.setBaseShapesFilled(true);
//			renderer.setDrawSeriesLineAsPath(true);
//		}
//
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy kk:mm", Locale.ENGLISH));

		return chart;

	}

	class WindowMenu extends JMenuBar {

		private static final long serialVersionUID = -1343969840383495517L;
		
		private JMenu menuFile;
		//private JMenu menuEdit;
		//private JMenu menuSkin;

		public WindowMenu() {

			// create file menu
	        menuFile = (JMenu)this.add(new JMenu("File"));
	        menuFile.setMnemonic('F');
	        if (true) {
		        JMenuItem openItem = menuFile.add("Open File...");
		        openItem.addActionListener(new OpenAction());	        	
	        }

	        JMenuItem openDbItem = menuFile.add("Open DB...");
	        openDbItem.addActionListener(new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					readData();      			
				}
			});	 
	        
	        JMenuItem importItem = menuFile.add("Import");
	        importItem.addActionListener(new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Frame frame = new Frame();
					FileDialog dialog = new FileDialog(frame, "Import...", FileDialog.LOAD);
					frame.setLocation(350,150);
					// set default data directory
					//dialog.setDirectory();
					dialog.setVisible(true);

					if (dialog.getFile() != null) {
						String dir = dialog.getDirectory();
						String name = dialog.getFile();
						logger.sayOut("Dir:  "+dir);
						logger.sayOut("Name: "+name);
						importFile(dir+name);
					}            			
				}
			});	 
	        
	        JMenuItem updateItem = menuFile.add("Refresh");
	        updateItem.addActionListener(new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					refreshStockData();
				}
			});	 
	        
	        menuFile.addSeparator();	        

	        JMenuItem exitItem = menuFile.add("Exit");
	        exitItem.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
	        	
	        });	    
		
 	        setVisible(true);

		}
		
		class OpenAction extends AbstractAction {
			private static final long serialVersionUID = 3937695439391547394L;

			public void actionPerformed(ActionEvent e) {
				Frame frame = new Frame();
				FileDialog dialog = new FileDialog(frame, "Open...", FileDialog.LOAD);
				frame.setLocation(350,150);
				// set default data directory
				//dialog.setDirectory();
				dialog.setVisible(true);

				if (dialog.getFile() != null) {
					String dir = dialog.getDirectory();
					String name = dialog.getFile();
					logger.sayOut("Dir:  "+dir);
					logger.sayOut("Name: "+name);
					readFile(dir+name);
				}            			
			}
		}

	}
		

	public class SeriesFilterPanel extends TitledBorderPanel {

		private InputDatePanel showStart;
		private InputDatePanel showEnd;

		private Date filterStartDate;
		private Date filterEndDate;

		//private DataRecordSeriesTable seriesRecords;
		private boolean filterDateChanged = false;
		private JPanel datePanel = new JPanel();
		private JPanel buttonsPanel = new JPanel();
		private JButton filterButton;
		private JButton resetButton;
		private JButton last50dButton;
		private JButton last200dButton;


		public SeriesFilterPanel() {
			super("Filter Data Series");
			initialize();
		}

		private void initialize() {
			// create all components and add them
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(1600, 60));
			datePanel.setLayout(new GridLayout(1, 3));
			buttonsPanel.setLayout(new GridLayout(1, 4));

			showStart = new InputDatePanel("Start Date");
			showEnd = new InputDatePanel("End Date");
			filterButton = new JButton("filter");
			resetButton = new JButton("reset");
			last50dButton = new JButton("50 days");
			last200dButton = new JButton("200 days");
			buttonsPanel.add(filterButton);
			buttonsPanel.add(last50dButton);
			buttonsPanel.add(last200dButton);
			buttonsPanel.add(resetButton);
			filterButton.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					updateDateRange();

				}
			});
			resetButton.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					resetDateRange();

				}
			});
			last50dButton.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(showStart.getDateValue() != null) {
						showStart.setDateValueDifference(50);
						filterStartDate = showStart.getDateValue();
						filterDateChanged = true;
					}
					if(showEnd.getDateValue() != null) {
						showEnd.setDateValueDifference(0);
						filterEndDate = showEnd.getDateValue();
						filterDateChanged = true;
					}
					updateDateRange();

				}
			});
			last200dButton.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(showStart.getDateValue() != null) {
						showStart.setDateValueDifference(200);
						filterStartDate = showStart.getDateValue();
						filterDateChanged = true;
					}
					if(showEnd.getDateValue() != null) {
						showEnd.setDateValueDifference(0);
						filterEndDate = showEnd.getDateValue();
						filterDateChanged = true;
					}
					updateDateRange();
				}
			});
			showStart.addFieldListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
				}

				@Override
				public void focusLost(FocusEvent e) {
					if(showStart.getDateValue() != null && !filterStartDate.equals(showStart.getDateValue())) {
						filterStartDate = showStart.getDateValue();
						filterDateChanged = true;
					}
				}

			});
			showEnd.addFieldListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
				}

				@Override
				public void focusLost(FocusEvent e) {
					if(showEnd.getDateValue() != null && !filterEndDate.equals(showEnd.getDateValue())) {
						filterEndDate = showEnd.getDateValue();
						filterDateChanged = true;
					}
				}

			});
			datePanel.add(showStart);
			datePanel.add(showEnd);
			datePanel.add(buttonsPanel);

			add(datePanel, BorderLayout.CENTER);
		}

		protected void updateDateRange() {
			if(filterDateChanged) {
				stockManager.setStartEndDate(filterStartDate,filterEndDate);	
				filterDateChanged = false;
				model.fireTableDataChanged();
			}
		}

		protected void resetDateRange() {
			stockManager.resetStartEndDate();	
				filterDateChanged = false;
				filterStartDate = stockManager.getStartDate();
				filterEndDate = stockManager.getEndDate();
				showStart.setDateValue(filterStartDate);
				showEnd.setDateValue(filterEndDate);
				model.fireTableDataChanged();
			}

//		public void setupDataSeriesTable(int datasetType) {
//		filterStartDate = stockManager.getStartDate();
//		filterEndDate = stockManager.getEndDate();
//		showStart.setDateValue(filterStartDate);
//		showEnd.setDateValue(filterEndDate);
//		addSeriesToTable();
//		JFreeChart chart = null;
//		if(datasetType == DataRecordSeriesTable.TABLE_TYPE_TIMESERIES) {
//			chart = createTimeSeriesChart(seriesRecords.getTimeSeriesDataset(), null, yAxisLabel);
//		}
//		if(datasetType == DataRecordSeriesTable.TABLE_TYPE_CATEGORY) {
//			chart = createCategoryChart(seriesRecords.getCategoryDataset(), null, yAxisLabel);
//		}
//		if(datasetType == DataRecordSeriesTable.TABLE_TYPE_BOXWHISKER) {
//			chart = createBoxAndWhiskersChart(seriesRecords.getBoxAndWhiskerCategoryDataset(), null, yAxisLabel);
//		}
//		chartPanel.setChart(chart);
//		chartPanel.setFillZoomRectangle(true);
//		chartPanel.setMouseWheelEnabled(true);
//	}
		
		public void setupSeriesDate() {
			filterStartDate = stockManager.getStartDate();
			filterEndDate = stockManager.getEndDate();
			showStart.setDateValue(filterStartDate);
			showEnd.setDateValue(filterEndDate);
		}

	}

	public class DataTablePanel extends TitledBorderPanel implements TableModelListener {

		//TitledBorderPanel tablePanel = new TitledBorderPanel("Stock Data Table");



		public DataTablePanel() {
			super("Stock Data");
			initialize();
		}

		private void initialize() {
			// create all components and add them
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(1400, 400));
			//model = new StockTableModel();
			model = new StockSeriesTableModel();
			dataTable= new JTable(model);
			model.addTableModelListener(this);
			dataTable.setDefaultRenderer(Double.class, new StockTableRenderer());
			dataTable.setDefaultRenderer(BigDecimal.class, new StockTableRenderer());

			add(new JScrollPane(dataTable), BorderLayout.CENTER);  			
		}

//		@Override
//		public void tableChanged(TableModelEvent e) {
//			TableModelEvent tme = e;
//			logger.sayOut("tableChanged Event called for row/column: " + tme.getFirstRow() + "/" + tme.getColumn());
//			if(tme.getColumn()!=0) {
//				return;
//			}
//			StockRowItem sri = (StockRowItem) model.getRow(tme.getFirstRow());
//			StockItem si = sri.getStockItem();;
//			if(si.getShow()) {
//				dataSeries.addSeries(sri.getTimeSeries());
//				ohlcSeriesCollection.addSeries(sri.getOHLCSeries());
//			} else {
//				dataSeries.removeSeries(sri.getTimeSeries());
//				ohlcSeriesCollection.removeSeries(sri.getOHLCSeries());
//			}
//			
//		}
		@Override
		public void tableChanged(TableModelEvent e) {
			TableModelEvent tme = e;
			logger.sayOut("tableChanged Event called for row/column: " + tme.getFirstRow() + "/" + tme.getColumn());
			if(tme.getColumn()!=0) {
				return;
			}
			StockDataSeries sds = (StockDataSeries) model.getRow(tme.getFirstRow());
			if(sds.getSeriesShow()) {
				dataSeries.addSeries(sds.getTimeSeries());
				ohlcSeriesCollection.addSeries(sds.getOHLCSeries());
			} else {
				dataSeries.removeSeries(sds.getTimeSeries());
				ohlcSeriesCollection.removeSeries(sds.getOHLCSeries());
			}
			
		}


	}






	class TitledBorderPanel extends JPanel {
		
		private static final long serialVersionUID = -6256502421400216512L;
	
		public TitledBorderPanel(String title) {
			Border etched = BorderFactory.createEtchedBorder();
			Border border = BorderFactory.createTitledBorder(etched, title);
			
			setBorder(border);
		}
	}

	class SetupDataThread extends PlotThread {
		
		public SetupDataThread() {
			super();
			pi.setIndeterminate(true);
		}
		
		public void run() {
			model = new StockSeriesTableModel();
			model.addTableModelListener(stockTable);
			dataTable.setModel(model);
			dataSeries.removeAllSeries();
			ohlcSeriesCollection.removeAllSeries();
			
			List<StockEntry> stocks = stockEntityManager.getStockEntries();
			for(StockEntry se : stocks) {
				update("Update Stock: " + se.getSymbol());
				stockManager.update(se.getSymbol(), model, dataSeries, ohlcSeriesCollection);
			}
			stockManager.findSeriesStartEndDate();
			filterPanel.setupSeriesDate();
			
//			update("Getting Stock Data...");
//			int cnt = stockManager.update(model, dataSeries, ohlcSeriesCollection);
//			if (cnt == 0) {
//				update("No Stock Data Series was created.");
//				finish();
//				return;
//			}
//			filterPanel.setupSeriesDate();
			
			update("Creating Chart Data...");
			JFreeChart chart = null;
			chart = createTimeSeriesChart(dataSeries, null, "Price");
			//chart = createOHLCSeriesChart(ohlcSeriesCollection, null, "Price");
			chartPanel.setChart(chart);
			chartPanel.setFillZoomRectangle(true);
			chartPanel.setMouseWheelEnabled(true);
			update("Done.");
			finish();
		}
		
	}

	class RefreshDataThread extends PlotThread {
		
		public RefreshDataThread() {
			super();
			pi.setIndeterminate(true);
		}
		
		public void run() {
			model = new StockSeriesTableModel();
			model.addTableModelListener(stockTable);
			dataTable.setModel(model);
			dataSeries.removeAllSeries();
			ohlcSeriesCollection.removeAllSeries();
			List<StockEntry> stocks = stockEntityManager.getStockEntries();
			for(StockEntry se : stocks) {
				update("Update Stock: " + se.getSymbol());
				stockEntityManager.updateStock(se.getSymbol());
				stockManager.update(se.getSymbol(), model, dataSeries, ohlcSeriesCollection);
			}
			stockManager.findSeriesStartEndDate();
			filterPanel.setupSeriesDate();
			update("Creating Chart Data...");
			JFreeChart chart = null;
			chart = createTimeSeriesChart(dataSeries, null, "Price");
			//chart = createOHLCSeriesChart(ohlcSeriesCollection, null, "Price");
			chartPanel.setChart(chart);
			chartPanel.setFillZoomRectangle(true);
			chartPanel.setMouseWheelEnabled(true);
			update("Done.");
			finish();
		}
		
	}

	class PlotThread extends Thread {
		ProgressInfo pi;
		int sleepTime = 5000;

		public PlotThread() {
			/**
			 * progressInfo in ProjectExplorer
			 */
			//pi = controller.getProgressInfo();

			/**
			 * progressInfo in Viz2D
			 */
			pi = getProgressInfo();
			pi.start();

			// setting cursor to wait mode
			//setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		}

		public void update(final String msg) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					pi.setNote(msg);
					pi.setIndeterminate(true);
				}

			});
		}

		public void update(final String msg, final int value) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					pi.setNote(msg);
					pi.setProgress(value);
				}

			});
		}

		public void finish() {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					pi.setProgress(100);
					reset();
				}

			});
		}

		public boolean isCancelled() {
			return pi.isCancelled();
		}

		public void reset() {
			pi.reset();
			//Toolkit.getDefaultToolkit().beep();
			//setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));	
		}
	}






}
