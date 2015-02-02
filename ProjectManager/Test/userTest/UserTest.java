package dataAccess;

import obj.Project;
import obj.User;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.*;

import static org.junit.Assert.*;

public class UserTest {

    private User _user1;
    private User _user2;
    private User _user3;
    
    @Before
    public void beforeTest() throws Exception 
    {
    	_user1 = new User(1, "Philippe", "GENOIS", "1", 0);
    	_user2 = new User(2, "David", "JAN", "2", 0);
    	_user3 = new User(3, "Francis", "FARA", "3", 1);
    }

    @Test
    public void testGetID() throws Exception
    {
    	assertTrue("Test user1 ID", 1, _user1.getId());
    	assertTrue("Test user2 ID", 2, _user2.getId());
    	assertTrue("Test user3 ID", 3, _user3.getId());

    	assertFalse("Test user1 ID", -1, _user1.getId());
    	assertFalse("Test user2 ID", 0, _user2.getId());
    	assertFalse("Test user3 ID", 2, _user3.getId());
    }

    @Test
    public void testGetUsername() throws Exception 
    {
    	assertTrue("Testing user1 username", _user1.getUsername().equal("1"));
    	assertTrue("Testing user2 username", _user2.getUsername().equal("2"));
    	assertTrue("Testing user3 username", _user3.getUsername().equal("3"));

    	assertFalse("Testing user1 username", _user1.getUsername().equal("Philippe"));
    	assertFalse("Testing user2 username", _user2.getUsername().equal("JAN"));
    	assertFalse("Testing user3 username", _user3.getUsername().equal("1"));
    }
    
    @Test
    public void testSetUsername() throws Exception
    {
    	_user1.setUsername("Neptas");
    	
    	assertTrue("Testing user1 username", _user1.getUsername().equal("Neptas"));
    	assertFalse("Testing user1 username", _user1.getUsername().equal("1"));  
    	
    	_user2.setUsername("Francis");
    	
    	assertFalse("Testing user2 username", _user2.getUsername().equal("FranciS"));
    	assertFalse("Testing user2 username", _user2.getUsername().equal("JAN"));    	
    }
    
    @Test
    public void testGetLastname() throws Exception
    {
    	assertTrue("Testing user1 lastname", _user1.getLastname().equal("GENOIS"));
    	assertTrue("Testing user2 lastname", _user2.getLastname().equal("JAN"));
    	assertTrue("Testing user3 lastname", _user3.getLastname().equal("FARA"));

    	assertFalse("Testing user1 lastname", _user1.getLastname().equal("Philippe"));
    	assertFalse("Testing user2 lastname", _user2.getLastname().equal("2"));
    	assertFalse("Testing user3 lastname", _user3.getLastname().equal("Francis"));
    }
    
    @Test
    public void testSetLastname() throws Exception
    {
    	_user1.setLastname("NEWNAME");

    	assertTrue("Testing user1 lastname", _user1.getLastname().equal("NEWNAME"));
    	assertFalse("Testing user1 lastname", _user1.getLastname().equal("NENAME"));    
    	
    	_user2.setLastname("NENAME");

    	assertFalse("Testing user2 lastname", _user2.getLastname().equal("NEWNAME"));
    	assertTrue("Testing user2 lastname", _user2.getLastname().equal("NENAME"));
    }
    
    @Test
    public void testGetFirstname() throws Exception
    {
    	assertTrue("Testing user1 firstname", _user1.getFirstname().equal("Philipe"));
    	assertTrue("Testing user2 firstname", _user2.getFirstname().equal("David"));
    	assertTrue("Testing user3 firstname", _user3.getFirstname().equal("Francis"));

    	assertFalse("Testing user1 firstname", _user1.getFirstname().equal("Philppe"));
    	assertFalse("Testing user2 firstname", _user2.getFirstname().equal("Dav"));
    	assertFalse("Testing user3 firstname", _user3.getFirstname().equal("Fr2ncis"));
    }
    
    @Test
    public void testSetFirstname() throws Exception
    {
    	_user1.setFirstname("");

    	assertTrue("Testing user1 firstname", _user1.getFirstname().equal(""));
    	assertFalse("Testing user1 firstname", _user1.getFirstname().equal("q"));
    	
    	_user2.setFirstname("David");

    	assertTrue("Testing user2 firstname", _user2.getFirstname().equal("David"));
    	assertFalse("Testing user2 firstname", _user2.getFirstname().equal(""));
    }
    
    @Test
    public void testGetRole() throws Exception
    {
    	assertTrue("Testing user1 Role", 0, _user1.getRole());
    	assertTrue("Testing user2 Role", 0, _user2.getRole());
    	assertTrue("Testing user3 Role", 1, _user3.getRole());

    	assertFalse("Testing user1 Role", 1, _user1.getRole());
    	assertFalse("Testing user2 Role", -1, _user2.getRole());
    	assertFalse("Testing user3 Role", 0, _user3.getRole());
    }
    
    @Test
    public void testSetRole() throws Exception
    {
    	_user1.setRole(1);
    	
    	assertTrue("Testing user1 Role", 1, _user1.getRole());
    	assertFalse("Testing user1 Role", 0, _user1.getRole());

    	_user2.setRole(1000);
    	
    	assertTrue("Testing user2 Role", 1000, _user2.getRole());
    	assertFalse("Testing user2 Role", -1000, _user2.getRole());
    }
}