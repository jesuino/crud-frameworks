package org.jugvale.crudframeworks.test;

import java.util.List;

import org.jugvale.crudframeworks.client.business.Framework;
import org.jugvale.crudframeworks.client.controller.FrameworkClientService;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Test to run with a clean database.
 * 
 * @author william
 * 
 */
public class FrameworksRESTClientTest {

	FrameworkClientService client;
	int validId = 0;
	
	@Before
	public void init() {
		System.out.println("Initiating tests...");
		client = new FrameworkClientService();
	}

	@Test
	public void testAll() {
		testAdd();
		testRetrieveAll();
		testRetrieve();
		testUpdate();
		testDelete();
	}

	public void testAdd() {
		System.out.println("Trying to add a new framework.");
		Framework f = new Framework();
		f.setName("Testing");
		client.add(f);
		System.out.println("Finished adding a new framework with success");
	}

	public void testRetrieve() {
		System.out.println("Trying to retrieve an existing framework.");
		System.out.println("Retrieved: " + client.get(validId).getName());
		System.out.println("Finished retrieving a framework with success");
	}

	public void testRetrieveAll() {
		System.out.println("Retrieving all frameworks");
		List<Framework> allFrameworks = client.getAll();
		System.out.println("All frameworks Ids: ");
		for (Framework framework : allFrameworks) {
			System.out.print(framework.getId() + " - ");
			// notice it's a simple way to have a valid id to work on next tests...
			validId = framework.getId();
		}
		System.out.println("\nRetrieved all frameworks with success");
	}

	public void testUpdate() {
		Framework f;
		System.out.println("Trying to update an existing framework.");
		System.out.println("Getting a framework..");
		f = client.get(validId);
		System.out.println("Framework current name: " + f.getName());
		f.setName("New Name, changed in test");
		System.out.println("Name changed, attempt to update it in server");
		client.update(f);
		f = client.get(validId);
		System.out.println("New name update in server: " + f.getName());
		System.out.println("Finished updating framework with success");
	}

	public void testDelete() {
		System.out.println("Now trying to delete all frameworks");
		List<Framework> allFrame = client.getAll();
		for (Framework f : allFrame) {
			client.remove(f.getId());
		}
		System.out.println("Checking if last framework was really deleted");
		try {
			client.get(validId);
			System.err.println("It Wasn't deleted");
		} catch (Exception e) {
			System.out.println("Success removing frameworks");
		}
	}

}
