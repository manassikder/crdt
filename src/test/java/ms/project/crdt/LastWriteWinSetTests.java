package ms.project.crdt;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author MSikder
 *
 */
public class LastWriteWinSetTests {
	
	long time1 = 1;
    long time2 = 2;
    long time3 = 3;
	 /**
	 * Test the lookup of data when its added and deleted in with current time
	 */
	@Test
     public void testLookupRealTime() {
		//create data set
		 
        final LastWriteWinSet<String> lastWriteWinSet = new LastWriteWinSet<>();

        lastWriteWinSet.add("java");
        lastWriteWinSet.add("scala");
        lastWriteWinSet.add("php");
        lastWriteWinSet.add("python");
        try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			//DO NOTHING
		}
        lastWriteWinSet.delete("scala");
        lastWriteWinSet.delete("java");
        

        // run test
        final Set<String> lookup = lastWriteWinSet.getData();

        assertTrue(lookup.size() == 2);
        assertTrue(lookup.contains("php"));
        assertTrue(lookup.contains("python"));
    }
	 
	/**
	 * Test the lookup of data when its added and deleted in with test time 
	 */
    @Test
    public void testLookup() {
    	
    	//create data set
        final LastWriteWinSet<String> lastWriteWinSet1 = new LastWriteWinSet<>();

        lastWriteWinSet1.add(time1, "java");
        lastWriteWinSet1.add(time1, "php");
        lastWriteWinSet1.add(time1, "python");
        lastWriteWinSet1.add(time2, "scala");

        lastWriteWinSet1.delete(time1, "scala");
        lastWriteWinSet1.delete(time2, "java");

        // run test
        final Set<String> lookup1 = lastWriteWinSet1.getData();

        assertTrue(lookup1.size() == 3);
        assertTrue(lookup1.contains("php"));
        assertTrue(lookup1.contains("python"));
        assertTrue(lookup1.contains("scala"));
    	
    	//create data set
        final LastWriteWinSet<String> lastWriteWinSet2 = new LastWriteWinSet<>();

        lastWriteWinSet2.add(time1, "java");
        lastWriteWinSet2.add(time1, "scala");
        lastWriteWinSet2.add(time1, "php");
        lastWriteWinSet2.add(time1, "python");

        lastWriteWinSet2.delete(time2, "scala");
        lastWriteWinSet2.delete(time2, "java");

        // run test
        final Set<String> lookup = lastWriteWinSet2.getData();

        assertTrue(lookup.size() == 2);
        assertTrue(lookup.contains("php"));
        assertTrue(lookup.contains("python"));
            
    }
   
    /**
	 * Test the equals of LWW set
	 */
    @Test
    public void testEquals() {
    	
    	//create data set
    	String strJava = "java";
    	String strScala = "scala";
    	String strPhp = "php";
    	
    	final LastWriteWinSet<String> lastWriteWinSet1 = new LastWriteWinSet<>();

    	lastWriteWinSet1.add(time1, strJava);
    	lastWriteWinSet1.add(time1, strScala);
    	lastWriteWinSet1.add(time1, strPhp);

    	lastWriteWinSet1.delete(time2, strJava);
        
        final LastWriteWinSet<String> lastWriteWinSet2 = new LastWriteWinSet<>();

        lastWriteWinSet2.add(time1, strJava);
        lastWriteWinSet2.add(time1, strScala);
        lastWriteWinSet2.add(time1, strPhp);

        lastWriteWinSet2.delete(time2, strJava);
        
        final LastWriteWinSet<String> lastWriteWinSet3 = lastWriteWinSet2;
    	
        assertTrue(lastWriteWinSet1.equals(lastWriteWinSet2));
        assertTrue(lastWriteWinSet3.equals(lastWriteWinSet2));
        assertTrue(lastWriteWinSet1.equals(lastWriteWinSet1));
        assertTrue(!lastWriteWinSet1.equals(null));
    }

    /**
	 * Test the merge of LWW set
	 */
    @Test
    public void testMerge() {
    	//create data set
        final LastWriteWinSet<String> lastWriteWinSet1 = new LastWriteWinSet<>();
        lastWriteWinSet1.add(time3, "php");
        lastWriteWinSet1.add(time1, "java");
        lastWriteWinSet1.add(time1, "scala");
        lastWriteWinSet1.delete(time2, "scala");

        final LastWriteWinSet<String> lastWriteWinSet2 = new LastWriteWinSet<>();
        lastWriteWinSet2.add(time1, "php");
        lastWriteWinSet2.add(time1, "python");
        lastWriteWinSet2.add(time1, "scala");
        lastWriteWinSet2.delete(time2, "php");

        // run test
        final LastWriteWinSet<String> resultSet = lastWriteWinSet1.merge(lastWriteWinSet2);

        assertTrue(resultSet.getData().size() == 3);
        assertTrue(resultSet.getData().contains("php"));
        assertTrue(resultSet.getData().contains("python"));
        assertTrue(resultSet.getData().contains("java"));
       
        
        final GrowOnlySet<LastWriteWinSet.ItemTD<String>> resultAddSet = resultSet.getAddSet();
        final Set<LastWriteWinSet.ItemTD<String>> addedData = resultAddSet.getData();
        assertTrue(addedData.size() == 5);
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(1, "php")));
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(3, "php")));
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(1, "java")));
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(1, "python")));
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(1, "scala")));

        final GrowOnlySet<LastWriteWinSet.ItemTD<String>> resultDeleteSet = resultSet.getDeleteSet();
        final Set<LastWriteWinSet.ItemTD<String>> deletedData = resultDeleteSet.getData();
        assertTrue(deletedData.size() == 2);
        assertTrue(deletedData.contains(new LastWriteWinSet.ItemTD<>(2, "scala")));
        assertTrue(deletedData.contains(new LastWriteWinSet.ItemTD<>(2, "php")));
    }

    /**
	 * Test the diff of LWW set
	 */
    @Test
    public void testDiff() {
    	//create data set
        final LastWriteWinSet<String> lastWriteWinSet1 = new LastWriteWinSet<>();
        lastWriteWinSet1.add(time3, "php");
        lastWriteWinSet1.add(time1, "java");
        lastWriteWinSet1.add(time2, "python");
        lastWriteWinSet1.add(time1, "scala");
        lastWriteWinSet1.delete(time2, "scala");

        final LastWriteWinSet<String> lastWriteWinSet2 = new LastWriteWinSet<>();
        lastWriteWinSet2.add(time1, "php");
        lastWriteWinSet2.add(time3, "python");
        lastWriteWinSet2.add(time1, "scala");
        lastWriteWinSet2.delete(time2, "php");

        // run test
        final LastWriteWinSet<String> resultSet = lastWriteWinSet1.diff(lastWriteWinSet2);
        
        assertTrue(resultSet.getData().size() == 3);
        assertTrue(resultSet.getData().contains("php"));
        assertTrue(resultSet.getData().contains("python"));
        assertTrue(resultSet.getData().contains("java"));
        
        final GrowOnlySet<LastWriteWinSet.ItemTD<String>> resultAddSet = resultSet.getAddSet();
        final Set<LastWriteWinSet.ItemTD<String>> addedData = resultAddSet.getData();
        assertTrue(addedData.size() == 3);
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(3, "php")));
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(1, "java")));
        assertTrue(addedData.contains(new LastWriteWinSet.ItemTD<>(2, "python")));

        final GrowOnlySet<LastWriteWinSet.ItemTD<String>> resultDeleteSet = resultSet.getDeleteSet();
        final Set<LastWriteWinSet.ItemTD<String>> deletedData = resultDeleteSet.getData();
        assertTrue(deletedData.size() == 1);
        assertTrue(deletedData.contains(new LastWriteWinSet.ItemTD<>(2, "scala")));
    }
}
