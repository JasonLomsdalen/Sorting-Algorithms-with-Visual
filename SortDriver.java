//Jason Lomsdalen - Assignment 3 - Buckalew
import java.awt.*;
import java.applet.Applet;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class SortDriver extends Applet 
{
   	// array to be sorted
   	private static int array[];  

   	private static int largestNum; // need to know for color scaling purposes in paint()

   	//flag to tell paint() whether to paint a single location or the whole array
   	private enum PaintType {ALL, RANGE, SINGLE};
   	private PaintType doPaint = PaintType.ALL;

	// index of single array location to be painted
   	private int index = -1;  
   	// left end of range to be drawn
   	private int leftRange = -1;  
   	// right end of range to be drawn
   	private int rightRange = -1;  
   
   	//this listener object responds to button events
   	private ButtonActionListener buttonListener;
   
   	//button to start the sort
   	private JButton startSort;
   
   	// the picture of the sort will appear on this canvas
   	private SortCanvas picture;
   	// size of the sort bar
   	private final int pictureWidth = 1001;  
   	private final int pictureHeight = 50;

   	// put buttons and canvas on this panel
   	private JPanel sortPanel;

   	// put radio buttons on this panel - sort types
   	private JPanel radioPanel;
   	//radio buttons - sorted order
   	private JPanel radioPanel2;
   	//radio label - sort times
   	private JPanel radioPanel3;

   	private JLabel selectSort;
   	private JLabel selectOrder;

   	// declarations for some more GUI elements
   	// a non-interactive text field
   	//Labels for the different sort times at the bottom
   	private JLabel bubbleTime;
   	private JLabel insertionTime;
   	private JLabel mergeTime;
	private JLabel quickTime;	

   	// radio buttons - sort algorithm
   	private JRadioButton sortBubble, sortInsertion, sortMerge, sortQuick; 
   	// radio buttons - sorted order
   	private JRadioButton inOrder, revOrder, randOrder; 
   	private ButtonGroup rButtons;

   	// you can type text into this field - set array size
   	private JTextField rText; 

   	//Varaible to keep track of which starting sort order was selected
   	private int sortOrder = 3;

   	//Variables to keep track of which sorting algorithm was selected
   	private int sortAlg = 1;
   	private static Random random = new Random();

   	//Size of array
   	private int size = 1000;

   	//Timing variables to track elapsed time
   	public static long firstTime;
	public static long finalTime;
	public static long elapsedTime;

	//Variables to keep track of each sort's most recent time
	public static long bubbleSortTime = 0;
	public static long insertionSortTime = 0;
	public static long mergeSortTime = 0;
	public static long quickSortTime = 0;
	 
   	public void init() 
   	{
	  	buttonListener = new ButtonActionListener();

	  	populateArrays(size, sortOrder);

	  	// set up the window
	  	sortPanel = new JPanel();
	  	sortPanel.setLayout(new BoxLayout(sortPanel, BoxLayout.Y_AXIS));
	  
	  	// first place the sort bar on top
	  	picture = new SortCanvas();
	  	sortPanel.add(picture);
	  
	  	// now place a button
	  	startSort = new JButton("Start");

	  	// the listener is triggered when the button is clicked
	  	startSort.addActionListener(buttonListener);
	  	sortPanel.add(startSort);

	  	// text field with room for 20 characters
	  	rText = new JTextField("Enter number to increase the size of Array", 40); 
	  	rText.addActionListener(buttonListener); 
	  	sortPanel.add(rText);

	  	// put these buttons in their own panel
	  	radioPanel = new JPanel();
	  	radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));

	  	//Select Sort Label
	  	selectSort = new JLabel("Select Sort: ");
	  	radioPanel.add(selectSort);
	  
	  	// radio buttons
	  	sortBubble = new JRadioButton("Bubble Sort", true); // true sets this button by default
	  	sortBubble.addActionListener(buttonListener);
	  	sortBubble.setForeground(Color.blue);
	  	radioPanel.add(sortBubble);

	  	sortInsertion = new JRadioButton("Insertion Sort", false); 
	  	sortInsertion.addActionListener(buttonListener);
	  	sortInsertion.setForeground(Color.blue);
	  	radioPanel.add(sortInsertion);

	  	sortMerge = new JRadioButton("Merge Sort", false); 
	  	sortMerge.addActionListener(buttonListener);
	  	sortMerge.setForeground(Color.blue);
	  	radioPanel.add(sortMerge);

	  	sortQuick = new JRadioButton("Quick Sort", false); 
	  	sortQuick.addActionListener(buttonListener);
	  	sortQuick.setForeground(Color.blue);
	  	radioPanel.add(sortQuick);

	  	// put these buttons in their own panel
	  	Color greenish = new Color(58,170,17);
	  	radioPanel2 = new JPanel();
	  	radioPanel2.setLayout(new BoxLayout(radioPanel2, BoxLayout.X_AXIS));

	  	radioPanel3 = new JPanel();
	  	radioPanel3.setLayout(new BoxLayout(radioPanel3, BoxLayout.X_AXIS));
	  

	  	//Select Sort Order
	  	selectSort = new JLabel("Select Sort Order: ");
	  	radioPanel2.add(selectSort);

	  	//Radio buttons for Array order prior to sorting
	  	inOrder = new JRadioButton("Sorted Order", false); 
	  	inOrder.addActionListener(buttonListener);
	  	inOrder.setForeground(greenish);
	  	radioPanel2.add(inOrder);

	  	revOrder = new JRadioButton("Reverse Order", false); 
	  	revOrder.addActionListener(buttonListener);
	  	revOrder.setForeground(greenish);
	  	radioPanel2.add(revOrder);

	  	randOrder = new JRadioButton("Random Order", true); 
	  	randOrder.addActionListener(buttonListener);
	  	randOrder.setForeground(greenish);
	  	radioPanel2.add(randOrder);

	  	// radio buttons for desired type of sorting algorithm
	  	rButtons = new ButtonGroup(); 
	  	rButtons.add(sortBubble);
	  	rButtons.add(sortInsertion);
	  	rButtons.add(sortMerge);
	  	rButtons.add(sortQuick);

	  	//radio buttons for desired type of starting array ordere
	  	rButtons = new ButtonGroup(); 
	  	rButtons.add(inOrder);
	  	rButtons.add(revOrder);
	  	rButtons.add(randOrder);

	  	//Print Labels for Timing and add them to their respective radio panel
	  	bubbleTime = new JLabel("Bubble Sort Time: " + bubbleSortTime + "ms   ");
	  	radioPanel3.add(bubbleTime);
	  	insertionTime = new JLabel("Insertion Sort Time: " + insertionSortTime + "ms   ");
	  	radioPanel3.add(insertionTime);
	  	mergeTime = new JLabel("Merge Sort Time: " + mergeSortTime + "ms   ");
	  	radioPanel3.add(mergeTime);
	  	quickTime = new JLabel("Quick Sort Time: " + quickSortTime + "ms   ");
	  	radioPanel3.add(quickTime);

	  	// now add the radio panels to the sort panel
	  	sortPanel.add(radioPanel);
	  	sortPanel.add(radioPanel2);
	  	sortPanel.add(radioPanel3);

	  	// add the panel to the window
	  	add(sortPanel);

	  	//Set background color
	  	setBackground(Color.GRAY);
	  
	  	//Paint Graphics
	  	picture.paint(picture.getGraphics());
  	}

	public void populateArrays(int size, int sortOrder)
	{
		//Create array with deseried size - 1000, unless otherwise entered
		array = new int[size];

		//Sorted Order
		if (sortOrder == 1)
		{
			largestNum = array[0] = 0;
			for (int i = 1; i < size; i++)
			{
				array[i] = i;
				if (array[i] > largestNum) 
				{
					largestNum = array[i];
				}
			}
		}
		
		//Reverse Order
		if (sortOrder == 2)
		{
			largestNum = array[0] = size;
			for (int i = size-1; i > 0; i--)
			{
				array[size-i] = i;
				if (array[size-i] > largestNum) 
				{
					largestNum = array[size-i];
				}
			}
		}

		//Random Order 
		if (sortOrder == 3)
		{
			largestNum = array[0] = (int) (Math.random()*1000000.0);
			for (int i = 1; i < size; i++)
			{
				array[i] = (int) (Math.random()*1000000.0);
				if (array[i] > largestNum) 
				{
					largestNum = array[i];
				}
			}
		}
	}

   	// this object is triggered whenever a button is clicked
   	private class ButtonActionListener implements ActionListener 
   	{
		public void actionPerformed(ActionEvent event) 
		{
			// find out which button was clicked 
			Object source = event.getSource();
			 
			// start sort button was clicked
			if (source == startSort) 
			{
				//Create Array with correct size and selected sort order
				populateArrays(size, sortOrder);

				//Reset Times before sort is called
				firstTime = 0;
				finalTime = 0;
				elapsedTime = 0;

				//doPaint = PaintType.ALL;
				//picture.paint(picture.getGraphics());

				// call the sort - based on which button option was selected
				//Bubble Sort
				if (sortAlg == 1)
				{
					doBubbleSort();
				}
				//Insertion Sort
				if (sortAlg == 2)
				{
					binaryInsertionSort(array, 0, size-1);
				}
				//Merge Sort
				if (sortAlg == 3)
				{
					mergeSort(array, 0, size-1);
				}
				//Quick Sort
				if (sortAlg == 4)
				{
					quickSort(array, 0, size-1);
				}
			}

			//Based on which button option was slected the type of sort gets defined
			//Bubble Sort
			if (source == sortBubble)
			{  
				sortAlg = 1;
			}
			//Insertion Sort
			if (source == sortInsertion)
			{
				sortAlg = 2;
			}
			//Merge Sort
			if (source == sortMerge)
			{
				sortAlg = 3;
			}
			//Quick Sort
			if (source == sortQuick)
		 	{
				sortAlg = 4;
			}

			//Based on which button option was slected the sorted order gets defined
		 	//Sorted Order
		 	if (source == inOrder)
		 	{
				sortOrder = 1;
				//Paints the bar containging the array in window
				rePaint();
			}
			//Reverse Order
			if (source == revOrder)
			{
				sortOrder = 2;
				rePaint();
			}
			//Random Order
			if (source == randOrder)
			{
				sortOrder = 3;
				rePaint();
			}

			// called when user hits return in text field - sets new size for the array
			if (source == rText) 
			{
				size = Integer.parseInt(rText.getText());
				//Create array with new specified size
				populateArrays(size,sortOrder);
			}
		}    
	}

   	private void doBubbleSort() 
   	{
   		//Takes current time
   		firstTime = System.currentTimeMillis();
	 	int temp;
	  	// this is just bubblesort
	  	for (int i=0; i<size-1; i++) 
	  	{
		 	for (int j=0; j<size-1-i; j++) 
		 	{
				if (array[j]>array[j+1]) 
				{
					//Swaps necessary elements 
			   		temp = array[j]; 
			   		array[j] = array[j+1]; 
			   		array[j+1] = temp;
			  
				   	// draws the bars between j and j+1
				   	doPaint = PaintType.RANGE;
				   	leftRange = j;
				   	rightRange = j+1;
				   	picture.paint(picture.getGraphics());
				}
		 	}
	  	}
	  	//Takes the time again after sorting
		finalTime = System.currentTimeMillis();
		//Subtracts firstTime from finalTime to get toal elapsed time = time for sort to complete
		elapsedTime = finalTime-firstTime;
		//Sets bubbblesort time to be printed on screen 
		bubbleSortTime = elapsedTime;
		//Prints time on screen
		printTime();
   	}    


   	private void binaryInsertionSort(int[] data, int first, int last)
   	{
   		//Timing stuff same as in Bubble Sort
   		firstTime = System.currentTimeMillis();
   		//Goes through data array
	 	for(int i = first+1; i <= last; i++)
	  	{
	  		//target gets index of where to stick element
		 	int target = binarySearch(data, data[i], first, i-1);
		 	if (target != i)
		 	{
				int temp = data[i];
				for(int j = i; j > target; j--)
				{
					//Swap elemetns
			   		data[j] = data[j-1];

			   		//Painting
			   		doPaint = PaintType.SINGLE;
				   	index = j;
				   	picture.paint(picture.getGraphics());
			   	}
				data[target] = temp;
		 	}
		 }
		finalTime = System.currentTimeMillis();
		elapsedTime = finalTime-firstTime;
		insertionSortTime = elapsedTime;
		printTime();
   }

   	// Use binary search to determine the index of the data array after which x should be inserted
   	private static int binarySearch(int[] data, int x, int first, int last)
   	{
		int middle = 0;
	  	while(first <= last)
	  	{
		 	middle = (first + last)/2;
		 	if (x < data[middle])
		 	{
				last = middle - 1;
			}
		 	else if (x > data[middle])
		 	{
				first = middle + 1;
			}
		 	else
		 	{
				return middle + 1;
			}  
	  	}
	  	if (x > data[middle])
	  	{
	  		return middle + 1;
	  	}
	  	else
	  	{
	  		return middle;
	  	}
   }

   	//Non recursive version of quicksort
   	private void quickSort(int[] data, int first, int last)
   	{
   		//Timing stuff same as in Bubble Sort
   		firstTime = System.currentTimeMillis();
   		int middle, size, p;
   		int[][] s =  new int [data.length][2];
   		//Keeps track of top of stack
      	int TOS = 0;
      	s[TOS][0] = first;
      	s[TOS][1] = last;

   		while (TOS > -1)
   		{
   			first = s[TOS][0];
   			last = s[TOS][1];
   			middle = (first + last)/2;
   			size = last - first + 1;

   			doPaint = PaintType.RANGE;
         	leftRange = s[TOS][0];
         	rightRange = s[TOS][1];
         	picture.paint(picture.getGraphics());

   			TOS--;

   			if(size < 2)
   			{
   				//Do Nothing because array of size 1 is already sorted
   			}
   			else
   			{
   				//find pivot
   				medianOfThree(data, first, last);
	   			p = partition(data, first, last);

	   			//Paint Array
	   			doPaint = PaintType.RANGE;  
	            leftRange = p; 
	            rightRange = p;
	            picture.paint(picture.getGraphics());

	            TOS++;
	            s[TOS][0] = p + 1;
	            s[TOS][1] = last;

	            TOS++;
	            s[TOS][0] = first;
	            s[TOS][1] = p - 1;
   			}
   		}
   		finalTime = System.currentTimeMillis();
		elapsedTime = finalTime-firstTime;
		quickSortTime = elapsedTime;
		printTime();
   	}

  	private static void medianOfThree(int[] data, int first, int last)  
   	{
	  	int temp,middle,median;
	  	if (last-first+1 < 3)
		 	return;
	  	middle = (first+last)/2;
	  	if (data[first] <= data[middle])
		 	if (data[middle] <= data[last])
				median = middle;
		 	else if (data[last] <= data[first])
				median = first;
		 	else
				median = last;
	  	else
		 	if (data[first] <= data[last])
				median = first;
		 	else if (data[last] <= data[middle])
				median = middle;
		 	else
				median = last;
	  	temp = data[first];
	  	data[first] = data[median];
	  	data[median] = temp;
    }

   	private static int partition(int[] data, int first, int last)
   	{
		int left = first+1;
	  	int right = last;
	  	int temp;
	  	while(true)
	  	{
		 	while(left <= right && data[left] <= data[first])
				++left;
		 	while(right >= left && data[right] > data[first])
				--right;
		 	if (left > right)
				break;
		 	temp = data[left];
		 	data[left] = data[right];
		 	data[right] = temp;
		 	++left;
		 	--right;
	  	} 
	  	temp = data[first];
	  	data[first] = data[right];
	  	data[right] = temp;
	  	return right;
	}

	//Recursive version of merge sort
	public void mergeSort(int[] data, int first, int last)
	{	
		//Timing stuff same as in Bubble Sort
		firstTime = System.currentTimeMillis();
		if (first < last)
		{
			int middle = first + (last - first) / 2;
			mergeSort(data, first, middle);
			mergeSort(data, middle+1, last);
			mergeTogether(data, first, middle, last);
		}

		//Paint array
		doPaint = PaintType.RANGE;
		leftRange = first;
	   	rightRange = last;
	   	picture.paint(picture.getGraphics());

	   	finalTime = System.currentTimeMillis();
		elapsedTime = finalTime-firstTime;
		mergeSortTime = elapsedTime;
		printTime();
	}

	public void mergeTogether(int[] data, int left, int middle, int right)
	{
		// Find sizes of two subarrays to be merged
        int leftSize = middle - left + 1;
        int rightSize = right - middle;
 
        // Create temp arrays
        int Left[] = new int [leftSize];
        int Right[] = new int [rightSize];
 
        //Copy data to Left and Right arrays
        for (int i =0 ; i < leftSize; i++)
        {
        	Left[i] = data[left + i];
        }
        for (int j = 0; j < rightSize; j++)
        {
        	Right[j] = data[middle + 1+ j];
        }
            
		int i = 0;
		int j = 0;
		// Initial index of merged subarry array
 		int k = left;
        
        while (i < leftSize && j < rightSize)
        {
            if (Left[i] <= Right[j])
            {
                data[k] = Left[i];
                i++;
            }
            else
            {
                data[k] = Right[j];
                j++;
            }
            k++;
        }
 
        // Puts any remaining elements from Left array into the new merged array
        while (i < leftSize)
        {
            data[k] = Left[i];
            i++;
            k++;
        }
 
        // Puts any remaining elements from Right array into the new merged array
        while (j < rightSize)
        {
            data[k] = Right[j];
            j++;
            k++;
        }
	}

	//Rapaint array
	public void rePaint()
	{
		populateArrays(size, sortOrder);
		doPaint = PaintType.ALL;
		picture.paint(picture.getGraphics());
	}

	//Paint Sort Times
	public void printTime()
	{

	  	bubbleTime.setText("Bubble Sort Time: " + bubbleSortTime + " ms   ");
	  	insertionTime.setText("Insertion Sort Time: " + insertionSortTime + " ms   ");
	  	mergeTime.setText("Merge Sort Time: " + mergeSortTime + " ms   ");
	  	quickTime.setText("Quick Sort Time: " + quickSortTime + " ms   ");

	  
	  	picture.paint(picture.getGraphics());
	}
			 
   	class SortCanvas extends Canvas
   	{
		// this class paints the sort bar 
	  	SortCanvas() 
	  	{
		 	setSize(pictureWidth, pictureHeight);
		 	setBackground(Color.white);
	  	}
	   
	  	public void paint(Graphics g) 
	  	{
		 
		 	if (doPaint == PaintType.ALL) 
		 	{
				// paint whole array - this takes time so it shouldn't be done too frequently
				setBackground(Color.white);
				g.setColor(Color.white);
				g.fillRect(0, 0, pictureWidth, pictureHeight);
			
				for (int i=0; i<size; i++) 
				{
			   		// the larger the number, the brighter green it is
			   		// green is between 0.0 and 1.0
			   		// divide by the largest number to get a value between 0 and 1
			   		float green = (float)(array[i]/(float)largestNum);

			   		// clamp if necessary - it shouldn't be
			   		if (green<0f) 
			   			green = 0f;
			   		if (green>1f) 
			   			green = 1f;

			   		g.setColor(new Color(0.1f, 0.5f, green));

			   		// array location 0 is painted at left; 
			   		// array location limit-1 is painted to right
			   		//this is a single vertical line in the bar
			   		g.drawLine((int)(i*pictureWidth/size), 0, (int)(i*pictureWidth/size), pictureHeight);
				}
		 	}
		 
		 	else if (doPaint == PaintType.RANGE) 
		 	{
				for (int i=leftRange; i<=rightRange; i++) 
				{
			   		float green = (float)(array[i]/(float)largestNum);
			   		if (green<0f) 
			   			green = 0f;
			   		if (green>1f) 
			   			green = 1f;

			   		g.setColor(new Color(0.1f, 0.5f, green));
			   		g.drawLine((int)(i*pictureWidth/size), 0, (int)(i*pictureWidth/size), pictureHeight);
				}
			}

			// just paint one location on the bar
		 	else 
		 	{   
				float green = (float)(array[index]/(float)largestNum);
				if (green<0f) 
					green = 0f;
				if (green>1f) 
					green = 1f;
				g.setColor(new Color(0.1f, 0.5f, green));
				g.drawLine((int)(index*pictureWidth/size), 0, (int)(index*pictureWidth/size), pictureHeight);
		 	}   
	  	}
   	}
}