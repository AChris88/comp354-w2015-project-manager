/**
 * 
 */
package unitTests;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import da.DatabaseManager;

/**
 * @author George Lambadas 7077076
 * 
 */
public class DatabaseControllerTest {
	DatabaseManager db;

	@Before
	public void createDatabaseController() {
		// in reality, this information will be extracted from the Properties
		// file through the ConfigurationController
		db = new DatabaseController(
				"jdbc:mysql://waldo.dawsoncollege.qc.ca:3306/D1011481",
				"D1011481", "dexentyr");
		db.setTablesWithTestData();
	}

	@Test
	public void testQueryingContact() {
		Vector<Contact> contacts = db.queryTableContact(null, "first_name");
		assertEquals("Jane", contacts.get(0).getFirstName());

	}

	@Test
	public void testQueryingContactWithWhere() {
		Vector<Contact> contacts = db.queryTableContact("first_name = 'Pat'",
				"first_name");
		for (Contact c : contacts) {
			assertEquals("Pat", c.getFirstName());
		}

	}

	@Test
	public void testQueryingFolder() {
		Vector<Folder> folders = db.queryTableFolder(null, "folder_name");
		assertEquals(4, folders.get(3).getFolderId());

	}

	@Test
	public void testQueryingFolderWithWhere() {
		Vector<Folder> folders = db.queryTableFolder("folder_name = 'Inbox'",
				"folder_name");
		assertEquals(1, folders.get(0).getFolderId());
	}

	@Test
	public void testQueryingMessage() {
		Vector<Email> messages = db.queryTableMessage(null, null);
		assertEquals("Kitten", messages.get(2).getSubject());
	}

	@Test
	public void testQueryingMessageWithWhere() {
		Vector<Email> messages = db.queryTableMessage(
				"message_subject = 'Hello'", null);
		for (Email e : messages)
			assertEquals("Hello", e.getSubject());
	}

	/**
	 * Performs 3 inserts on the contacts table, of which 1 should fail
	 */
	@Test
	public void testInsertContacts() {
		int before = db.queryTableContact(null, "first_name").size();
		ArrayList<Contact> contacts = new ArrayList<Contact>();

		contacts.add(new Contact(0, "George", "Lambadas", "g.thug@gmail.com"));

		// should not reinsert Rick James
		contacts.add(db.queryTableContact("first_name = 'Rick'", "first_name")
				.get(0));

		contacts.add(new Contact(0, "Geo", "Lamb", "kitten@gmail.com"));

		db.insertIntoContact(contacts);

		int after = db.queryTableContact(null, "first_name").size();

		// ensure only 2 contacts were added
		assertEquals(before + 2, after);

		assertEquals(1,
				db.queryTableContact("first_name = 'Rick'", "first_name")
						.size());
	}

	@Test
	public void testDeleteContacts() {
		Vector<Contact> contacts = db.queryTableContact("first_name = 'Geo'",
				"first_name");

		for (Contact c : contacts)
			db.deleteFromContact(c.getId());

		contacts = db.queryTableContact("first_name = 'Geo'", "first_name");
		assertEquals(0, contacts.size());

		contacts = db.queryTableContact("first_name = 'George'", "first_name");

		for (Contact c : contacts)
			db.deleteFromContact(c.getId());

		contacts = db.queryTableContact("first_name = 'George'", "first_name");
		assertEquals(0, contacts.size());

	}

	@Test
	public void testUpdatingContacts() {
		Vector<Contact> contacts;

		contacts = db.queryTableContact("first_name = 'Rick'", "first_name");
		assertEquals(1, contacts.size());
		Contact c = contacts.get(0);

		c.setFirstName("Richard");
		db.updateContact(c);

		contacts = db.queryTableContact("first_name = 'Rick'", "first_name");
		assertEquals(0, contacts.size());

		contacts = db.queryTableContact("first_name = 'Richard'", "first_name");
		assertEquals(1, contacts.size());

	}

	@Test
	public void testInsertFolder() {
		String folderName = "new folder";
		Vector<Folder> folders1 = db.queryTableFolder(null, null);
		db.insertIntoFolder(folderName);

		Vector<Folder> folders2 = db.queryTableFolder(null, null);
		assertEquals(folders1.size() + 1, folders2.size());

		assertEquals(folderName, folders2.get(folders2.size() - 1).getName());

	}

	@Test
	public void testDeleteFolder() {

		Vector<Folder> folders1 = db.queryTableFolder(null, null);
		Folder f = folders1.get(folders1.size() - 1);

		// delete last folder
		db.deleteFromFolder(f.getFolderId());

		// 1 less record
		Vector<Folder> folders2 = db.queryTableFolder(null, null);
		assertEquals(folders2.size(), folders1.size() - 1);

		// db no longer contains folder of that name
		for (Folder aFolder : folders2)
			assertFalse(f.getName().equals(aFolder.getName()));

	}

	@Test
	public void testDeleteFolderNonExistent() {

		Vector<Folder> folders1 = db.queryTableFolder(null, null);

		// delete non existent folder
		db.deleteFromFolder(12);

		// 1 less record
		Vector<Folder> folders2 = db.queryTableFolder(null, null);
		assertEquals(folders2.size(), folders1.size());

		// db no longer contains folder of that name

		// folders are all the same
		for (int i = 0; i < folders2.size(); i++)
			assertEquals(folders1.get(i).getName(), folders2.get(i).getName());

	}

	// MAIL CRUD
	@Test
	public void testInsertMessage() {
		Vector<Email> messages = db.queryTableMessage(null, null);
		ArrayList<String> to, cc, bcc;
		// create new email
		to = new ArrayList<String>();
		to.add("j.smith@gmail.com");
		to.add("p.black@hotmail.com");

		cc = new ArrayList<String>();
		cc.add("w.white@yahoo.ca");

		bcc = new ArrayList<String>();
		bcc.add("s.colbert@live.ca");
		Email e = new Email(0, new Date(), "p.smith@gmail.com", to, cc, bcc,
				"testing", "do not be alarmed, this is a test", "1");

		ArrayList<Email> emailMessages = new ArrayList<Email>();
		emailMessages.add(e);

		// insert new email
		db.insertIntoMessage(emailMessages);
		Vector<Email> messages2 = db.queryTableMessage(null, null);

		// check for 1 more row selected
		assertEquals(messages.size() + 1, messages2.size());

		// compare new email's body to inserted email's body
		assertEquals(e.getBody(), messages2.get(messages2.size() - 1).getBody());

	}

	@Test
	public void testDeleteMessage() {
		Vector<Email> messages = db.queryTableMessage(null, null);
		Email e = messages.get(messages.size() - 1);
		db.deleteFromMessage(e.getId());

		// get all messages left in db
		messages = db.queryTableMessage(null, null);

		// compare ids to email that was deleted
		for (Email m : messages)
			assertFalse(e.getId() == m.getId());

	}

	@Test
	public void testDeleteFakeMessage() {
		Vector<Email> messages = db.queryTableMessage(null, null), messages2;

		db.deleteFromMessage(9000);

		// get all messages left in db
		messages2 = db.queryTableMessage(null, null);

		assertEquals(messages.size(), messages2.size());

		// compare ids to email that was deleted
		for (int i = 0; i < messages.size(); i++)
			assertEquals(messages.get(i).getId(), messages2.get(i).getId());

	}

	@Test
	public void testUpdateMessage() {
		Vector<Email> messages = db.queryTableMessage(null, null), messages2;
		Email e = messages.get(0);
		String oldBody = e.getBody();
		e.setBody("this is a new body");
		db.updateMessage(e);

		// get all messages left in db
		messages2 = db.queryTableMessage(null, null);

		// nothing has been deleted
		assertEquals(messages.size(), messages2.size());

		// old body is no longer in the db
		for (int i = 0; i < messages.size(); i++)
			assertFalse(oldBody.equals(messages2.get(i).getBody()));
	}

}
