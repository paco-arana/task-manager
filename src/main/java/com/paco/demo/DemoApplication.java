package com.paco.demo;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

// Needed so we ccan receive requests from 8080
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8080")
@SpringBootApplication
@RestController
public class DemoApplication {

    private Map<Integer, Task> taskMap = new HashMap<>();
    private int nextTaskId = 1;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostMapping("/todos")
    public int create(@RequestBody Task task) throws Exception{

		// Text should be present
		if (task.text == null || task.text.isEmpty()) {
        throw new IllegalArgumentException("Task text is required.");
    	}
		// Priority should be an int between 1 and 3
    	if (task.priority < 1 || task.priority > 3) {
        throw new IllegalArgumentException("Task priority is required.");
    	}

        task.id = nextTaskId;
        task.completed = false;
        task.startDate = Math.toIntExact(System.currentTimeMillis() / 1000);

        taskMap.put(nextTaskId, task);
        nextTaskId++;

        return task.id;
    }

    @GetMapping("/todos")
    public Collection<Task> getAllTodos(
        @RequestParam(value = "sort", defaultValue = "none", required = false) String sort,
        @RequestParam(value = "filterCompleted", defaultValue = "none", required = false) String filterCompleted,
		@RequestParam(value = "filterPriority", defaultValue = "none", required = false) String filterPriority,
		@RequestParam(value = "keywords", defaultValue = "none", required = false) String keywords) {

		List<Task> sortedTasks = new ArrayList<>(taskMap.values());

		// We sort the tasks:
    	if (sort.equals("priority")) {
        sortedTasks.sort(Comparator.comparingInt(task -> task.priority));
    	} else if (sort.equals("due")) {
        sortedTasks.sort(Comparator.comparingInt(task -> {
            if (task.dueDate == 0) {
                return 100000000; // Tasks with no due date set are sent to year 10,000
            }
            return task.dueDate;
        	}));
    	} // Otherwise do nothing 

		// Filter the tasks by status:
		if (filterCompleted.equals("done")) {
			sortedTasks.removeIf(task -> task.completed == false);
		} else if (filterCompleted.equals("undone")) {
			sortedTasks.removeIf(task -> task.completed == true);
		} // Otherwise do nothing

		// Filter the tasks by priority:
		if (filterPriority.equals("high")) {
			sortedTasks.removeIf(task -> task.priority == 2 || task.priority == 3);
		} else if (filterPriority.equals("medium")) {
			sortedTasks.removeIf(task -> task.priority == 1 || task.priority == 3);
		} else if (filterPriority.equals("low")) {
			sortedTasks.removeIf(task -> task.priority == 1 || task.priority == 2);
		} // Otherwise do nothing

		// Search by keywords in the name:
		if (!keywords.equals("none")) {
    		// Remove tasks if task.text doesn't contain keywords
    		sortedTasks.removeIf(
				task -> !task.text.toLowerCase().contains(keywords.toLowerCase())
			);
		}

		return sortedTasks;
    }

    @PutMapping("/todos/{id}/done")
    public Task doneTodo(@PathVariable int id){
        Task taskFound = taskMap.get(id);
        taskFound.completed = true;
		taskFound.endDate = Math.toIntExact(System.currentTimeMillis() / 1000);
        return taskFound;
    }

	@PutMapping("/todos/{id}/undone")
    public Task undoTodo(@PathVariable int id){
        Task taskFound = taskMap.get(id);
        taskFound.completed = false;
		taskFound.endDate = 0;
        return taskFound;
    }

	@PutMapping("/todos/{id}")
    public Task updateTodo(@PathVariable int id, @RequestBody Task task) throws Exception {
        Task taskFound = taskMap.get(id);

		// Text should be present
		if (task.text == null || task.text.isEmpty()) {
        throw new IllegalArgumentException("Task text is required.");
    	}
		// Priority should be an int between 1 and 3
    	if (task.priority < 1 || task.priority > 3) {
        throw new IllegalArgumentException("Task priority is required.");
    	}

        taskFound.text = task.text;
        taskFound.priority = task.priority;
		taskFound.dueDate = task.dueDate;

        return taskFound;
    }

    @DeleteMapping("/todos/{id}")
    public Collection<Task> deleteTodo(@PathVariable int id) {
        taskMap.remove(id);
        return taskMap.values();
    }
}