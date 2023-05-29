package com.paco.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DemoApplicationTests {

    private DemoApplication demoApplication;

    @BeforeEach
    public void setup() {
        demoApplication = new DemoApplication();
    }

	// Test for adding new todos
    @Test
    public void testCreateTask() throws Exception {
        Task task = new Task();
        task.setText("Sample Task");
        task.setPriority(2);

        int taskId = demoApplication.create(task);
        Map<Integer, Task> taskMap = demoApplication.getTaskMap();

        assertTrue(taskMap.containsKey(taskId));
        assertEquals(task, taskMap.get(taskId));
    }

	// Test for fetching all todos
	@Test
    public void testGetAllTodos() {
        Collection<Task> tasks = demoApplication.getAllTodos("none", "none", "none", "none");
        assertNotNull(tasks);
    }

	// Test for updating todos
	@Test
    public void testUpdateTodo() throws Exception {
        Task task = new Task();
        task.setText("Sample Task");
        task.setPriority(2);

        int taskId = demoApplication.create(task);

        Task updatedTask = new Task();
        updatedTask.setText("Updated Task");
        updatedTask.setPriority(3);

        Task result = demoApplication.updateTodo(taskId, updatedTask);

        assertEquals(updatedTask.getText(), result.getText());
        assertEquals(updatedTask.getPriority(), result.getPriority());
    }

}